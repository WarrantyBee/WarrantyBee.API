using Microsoft.Extensions.Options;
using WarrantyBee.Application.Abstractions.Persistence;
using WarrantyBee.Shared.Infrastructure.Abstractions;
using WarrantyBee.Shared.Core.Utilities;
using WarrantyBee.Shared.Core.Configuration;
using WarrantyBee.Application.Contracts.Identity;
using WarrantyBee.Application.Contracts.Users;
using WarrantyBee.Shared.Core.Enums;
using WarrantyBee.Shared.Core.Exceptions;
using WarrantyBee.Application.Abstractions.Services;

namespace WarrantyBee.Application.Services;

/// <summary>
/// Service responsible for handling authentication and authorization operations.
/// </summary>
public class AuthService : IAuthService
{
    private readonly AppConfiguration _config;
    private readonly ITokenService _tokenService;
    private readonly ICacheService _cacheService;
    private readonly ICaptchaService _captchaService;
    private readonly IOtpService _otpService;
    private readonly ITelemetryService _telemetryService;
    private readonly IUserRepository _userRepository;
    private readonly IOtpRepository _otpRepository;
    private readonly IRefreshTokenRepository _refreshTokenRepository;
    private readonly IJobSchedulerClient _jobScheduler;
    private readonly IEventPublisher _eventPublisher;

    /// <summary>
    /// Initializes a new instance of the <see cref="AuthService"/> class.
    /// </summary>
    public AuthService(
        IOptions<AppConfiguration> config,
        ITokenService tokenService,
        ICacheService cacheService,
        ICaptchaService captchaService,
        IOtpService otpService,
        ITelemetryService telemetryService,
        IUserRepository userRepository,
        IOtpRepository otpRepository,
        IRefreshTokenRepository refreshTokenRepository,
        IJobSchedulerClient jobScheduler,
        IEventPublisher eventPublisher)
    {
        _config = config.Value;
        _tokenService = tokenService;
        _cacheService = cacheService;
        _captchaService = captchaService;
        _otpService = otpService;
        _telemetryService = telemetryService;
        _userRepository = userRepository;
        _otpRepository = otpRepository;
        _refreshTokenRepository = refreshTokenRepository;
        _jobScheduler = jobScheduler;
        _eventPublisher = eventPublisher;
    }

    public async Task<ILoginResponse> LoginAsync(LoginRequest request)
    {
        bool hasValidCaptcha = await _captchaService.ValidateAsync(request.CaptchaResponse!);
        if (!hasValidCaptcha) throw new ApiException(Errors.InvalidCaptcha);

        if (request is SimpleLoginRequest simpleRequest)
        {
            return await ProcessSimpleLoginAsync(simpleRequest);
        }
        if (request is MFALoginRequest mfaRequest)
        {
            ValidateMfaLogin(mfaRequest);
            return await ProcessMfaLoginAsync(mfaRequest);
        }

        throw new ApiException(Errors.InvalidRequestBody);
    }

    public async Task<LoginResponse> RefreshTokenAsync(RefreshTokenRequest request)
    {
        if (string.IsNullOrWhiteSpace(request.RefreshToken)) throw new ApiException(Errors.TokenRequired);

        var tokenHash = HashHelper.ComputeHash(request.RefreshToken);
        var record = await _refreshTokenRepository.GetByHashAsync(tokenHash);

        if (record == null) throw new ApiException(Errors.InvalidToken);

        // Security: Token Rotation and Replay Attack detection
        if (record.IsRevoked)
        {
            // Someone is trying to reuse a revoked token - potentially a stolen one!
            // Revoke all tokens for this user for safety.
            await _refreshTokenRepository.RevokeDescendantsAsync(tokenHash);
            throw new ApiException(Errors.SessionExpired);
        }

        if (record.ExpiresAt < DateTime.UtcNow) throw new ApiException(Errors.SessionExpired);

        // Valid token found. Process refresh.
        var user = await _userRepository.GetAsync(new UserSearchFilter(record.UserId, null));
        if (user == null) throw new ApiException(Errors.UserNotFound);

        // Create new tokens
        var loginResponse = await GetLoginResponseAsync(user);

        // Revoke the old one and link it to the new one
        var newRefreshTokenHash = HashHelper.ComputeHash(loginResponse.RefreshToken);
        await _refreshTokenRepository.RevokeAsync(record.Id, newRefreshTokenHash);

        return loginResponse;
    }

    public async Task<SignUpResponse> SignUpAsync(SignUpRequest request)
    {
        ValidateSignUpRequest(request);

        bool hasValidCaptcha = await _captchaService.ValidateAsync(request.CaptchaResponse!);
        if (!hasValidCaptcha) throw new ApiException(Errors.InvalidCaptcha);

        var existingUser = await _userRepository.GetAsync(new UserSearchFilter(null, request.Email));
        if (existingUser != null) throw new ApiException(Errors.UserAlreadyRegistered);

        if (request.AuthProvider == (byte)AuthProvider.Internal)
        {
            request.Password = HashHelper.GetHash(request.Password);
            request.AuthProviderUserId = string.Empty;
        }
        else
        {
            request.Password = string.Empty;
        }

        var userId = await _userRepository.CreateAsync(request);
        if (userId <= 0) throw new ApiException(Errors.UserRegistrationFailed);

        // 1. Trigger Event (EventManager) for audit and webhooks
        await _eventPublisher.PublishAsync("user.signup", new { UserId = userId, Email = request.Email });

        // 2. Schedule Notification (JobScheduler) using Smart ID pattern
        await _jobScheduler.EnqueueNotificationAsync(userId, NotificationType.WelcomeEmail);

        return new SignUpResponse(userId);
    }

    public async Task ForgotPasswordAsync(ForgotPasswordRequest request)
    {
        if (Validator.IsBlank(request.Email)) throw new ApiException(Errors.EmailRequired);
        if (!Validator.IsEmail(request.Email)) throw new ApiException(Errors.InvalidEmail);

        bool hasValidCaptcha = await _captchaService.ValidateAsync(request.CaptchaResponse!);
        if (!hasValidCaptcha) throw new ApiException(Errors.InvalidCaptcha);

        var user = await _userRepository.GetAsync(new UserSearchFilter(null, request.Email));
        if (user == null) throw new ApiException(Errors.UserNotRegistered);

        bool canReset = true;
        var lastUpdated = user.Profile?.Settings?.PasswordUpdatedAt;
        if (lastUpdated.HasValue)
        {
            var window = _config.Profile?.PasswordResetWindow ?? 0;
            if (lastUpdated.Value.AddMinutes(window) > DateTime.UtcNow)
            {
                canReset = false;
            }
        }

        if (!canReset) throw new ApiException(Errors.PasswordRecentlyUpdated);

        await SendOtpAsync(user.Id, user.Email ?? string.Empty, OtpRequestReason.ForgotPassword);
    }

    public async Task ResetPasswordAsync(ResetPasswordRequest request)
    {
        if (Validator.IsBlank(request.Otp)) throw new ApiException(Errors.OtpRequired);
        if (Validator.IsBlank(request.Email)) throw new ApiException(Errors.EmailRequired);
        if (Validator.IsBlank(request.NewPassword)) throw new ApiException(Errors.PasswordRequired);
        if (!Validator.IsStrongPassword(request.NewPassword)) throw new ApiException(Errors.StrongPasswordRequired);

        bool hasValidCaptcha = await _captchaService.ValidateAsync(request.CaptchaResponse!);
        if (!hasValidCaptcha) throw new ApiException(Errors.InvalidCaptcha);

        var user = await _userRepository.GetAsync(new UserSearchFilter(null, request.Email));
        if (user == null) throw new ApiException(Errors.UserNotRegistered);

        var storedOtpHash = await _otpRepository.GetAsync(new OtpSearchFilter(user.Email ?? string.Empty, user.Id, (byte)OtpRequestReason.ForgotPassword));
        if (!HashHelper.Verify(request.Otp, storedOtpHash!)) throw new ApiException(Errors.InvalidOtp);

        var previousPasswords = await _userRepository.GetPasswordsAsync(user.Id);
        if (previousPasswords.Any(p => HashHelper.Verify(request.NewPassword, p)))
        {
            throw new ApiException(Errors.PasswordAlreadyUsed);
        }

        var newHash = HashHelper.GetHash(request.NewPassword);
        var success = await _userRepository.ResetPasswordAsync(new PasswordResetRequest(user.Id, newHash));
        if (!success) throw new ApiException(Errors.PasswordResetFailed);

        // 1. Trigger Event (EventManager)
        await _eventPublisher.PublishAsync("user.password_reset", new { UserId = user.Id, Email = user.Email });

        // 2. Schedule Notification (JobScheduler)
        await _jobScheduler.EnqueueNotificationAsync(user.Id, NotificationType.PasswordChanged);
    }

    private async Task<ILoginResponse> ProcessSimpleLoginAsync(SimpleLoginRequest request)
    {
        ValidateSimpleLogin(request);

        var user = await _userRepository.GetAsync(new UserSearchFilter(null, request.Email));
        if (user == null) throw new ApiException(Errors.UserNotRegistered);

        // Default to Internal if not specified
        var authProvider = request.AuthProvider ?? (byte)AuthProvider.Internal;

        if (authProvider == (byte)AuthProvider.Internal)
        {
            if (string.IsNullOrWhiteSpace(user.Password) || !HashHelper.Verify(request.Password!, user.Password))
            {
                throw new ApiException(Errors.InvalidLoginCredentials);
            }
        }
        else
        {
            if (string.IsNullOrWhiteSpace(user.AuthProviderUserId) || request.AuthProviderUserId != user.AuthProviderUserId)
            {
                throw new ApiException(Errors.InvalidLoginCredentials);
            }
        }
        
        if (user.Profile?.Settings?.Is2FAEnabled == true)
        {
            var token = HashHelper.GenerateToken();
            var success = await _userRepository.StoreTokenAsync(new LoginTokenDetails(user.Id, token));
            if (!success) throw new ApiException(Errors.LoginTokenCouldNotBeSaved);

            await SendOtpAsync(user.Id, user.Email ?? string.Empty, OtpRequestReason.Login);
            return new MFALoginResponse(token);
        }

        await _eventPublisher.PublishAsync("user.login.success", new { UserId = user.Id, Email = user.Email });
        return await GetLoginResponseAsync(user);
    }

    private async Task<LoginResponse> ProcessMfaLoginAsync(MFALoginRequest request)
    {
        var user = await _userRepository.GetAsync(new UserSearchFilter(null, request.Email));
        if (user == null) throw new ApiException(Errors.UserNotRegistered);

        if (user.Profile?.Settings?.Is2FAEnabled != true) throw new ApiException(Errors.MfaNotEnabled);

        var isValidToken = await _userRepository.ValidateTokenAsync(new LoginTokenDetails(user.Id, request.Token!));
        if (!isValidToken) throw new ApiException(Errors.InvalidToken);

        var otpHash = await _otpRepository.GetAsync(new OtpSearchFilter(user.Email!, user.Id, (byte)OtpRequestReason.Login));
        if (!HashHelper.Verify(request.Otp!, otpHash!)) throw new ApiException(Errors.InvalidOtp);

        await _eventPublisher.PublishAsync("user.login.mfa.success", new { UserId = user.Id, Email = user.Email });
        return await GetLoginResponseAsync(user);
    }

    private async Task<LoginResponse> GetLoginResponseAsync(UserResponse user)
    {
        var claims = new Dictionary<string, object>
        {
            ["userId"] = user.Id.ToString(),
            ["email"] = user.Email!,
            ["role"] = user.AuthorizationContext?.Role.ToString() ?? "CUSTOMER"
        };

        // Unified RBAC: Add granular permissions to the token
        if (user.AuthorizationContext?.Permissions != null)
        {
            claims["permissions"] = string.Join(",", user.AuthorizationContext.Permissions.Select(p => p.ToString()));
        }

        var accessToken = _tokenService.Generate(claims);
        var refreshToken = HashHelper.GenerateToken(); // Random 64-char hex string
        
        // Store refresh token
        var refreshTokenHash = HashHelper.ComputeHash(refreshToken);
        var expiryDays = _config.Jwt?.RefreshTokenExpirationDays > 0 ? _config.Jwt.RefreshTokenExpirationDays : 7;
        await _refreshTokenRepository.AddAsync(user.Id, refreshTokenHash, DateTime.UtcNow.AddDays(expiryDays));

        var validatedClaims = _tokenService.Validate(accessToken);
        
        var iat = validatedClaims.ContainsKey("iat") ? validatedClaims["iat"].ToString()! : DateTime.UtcNow.ToString("O");
        var exp = validatedClaims.ContainsKey("exp") ? validatedClaims["exp"].ToString()! : DateTime.UtcNow.AddHours(1).ToString("O");

        return new LoginResponse(accessToken, refreshToken, iat, exp, user);
    }

    private async Task SendOtpAsync(long userId, string email, OtpRequestReason reason)
    {
        var otp = _otpService.Generate();
        var otpHash = HashHelper.GetHash(otp);
        await _otpRepository.StoreAsync(new OtpStorageRequest(otpHash, email, userId, (byte)reason));

        // 1. Trigger Event (EventManager)
        var eventType = reason == OtpRequestReason.Login ? "user.login.otp_sent" : "user.forgot_password.otp_sent";
        await _eventPublisher.PublishAsync(eventType, new { UserId = userId, Email = email });

        // 2. Schedule Notification (JobScheduler)
        var metadata = new Dictionary<string, string> 
        { 
            ["OTP"] = otp,
            ["EXPIRY_TIME"] = _config.Otp?.Expiration.ToString() ?? "5"
        };
        var type = reason == OtpRequestReason.Login ? NotificationType.LoginOtp : NotificationType.ForgotPasswordOtp;
        
        await _jobScheduler.EnqueueNotificationAsync(userId, type, metadata);
    }

    private void ValidateSignUpRequest(SignUpRequest request)
    {
        if (!request.HasAcceptedTermsAndConditions) throw new ApiException(Errors.TermsAndConditionsNotAccepted);
        if (!request.HasAcceptedPrivacyPolicy) throw new ApiException(Errors.PrivacyPolicyNotAccepted);
        if (Validator.IsBlank(request.Firstname)) throw new ApiException(Errors.FirstnameRequired);
        if (Validator.IsBlank(request.Lastname)) throw new ApiException(Errors.LastnameRequired);
        if (!Validator.IsEmail(request.Email)) throw new ApiException(Errors.InvalidEmail);
        if (request.AuthProvider == (byte)AuthProvider.Internal)
        {
            if (Validator.IsBlank(request.Password)) throw new ApiException(Errors.PasswordRequired);
            if (!Validator.IsStrongPassword(request.Password)) throw new ApiException(Errors.StrongPasswordRequired);
        }
        else if (Validator.IsBlank(request.AuthProviderUserId))
        {
            throw new ApiException(Errors.AuthProviderUserIdentifierRequired);
        }
    }

    private void ValidateSimpleLogin(SimpleLoginRequest request)
    {
        if (!Validator.IsEmail(request.Email)) throw new ApiException(Errors.InvalidEmail);
        
        var authProvider = request.AuthProvider ?? (byte)AuthProvider.Internal;
        if (authProvider == (byte)AuthProvider.Internal && Validator.IsBlank(request.Password))
            throw new ApiException(Errors.PasswordRequired);
    }

    private void ValidateMfaLogin(MFALoginRequest request)
    {
        if (!Validator.IsEmail(request.Email)) throw new ApiException(Errors.InvalidEmail);
        if (Validator.IsBlank(request.Token)) throw new ApiException(Errors.TokenRequired);
        if (Validator.IsBlank(request.Otp)) throw new ApiException(Errors.OtpRequired);
    }
}
