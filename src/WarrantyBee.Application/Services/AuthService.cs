using Microsoft.Extensions.Options;
using WarrantyBee.Application.Abstractions.Persistence;
using WarrantyBee.Application.Abstractions.Services;
using WarrantyBee.Application.Common;
using WarrantyBee.Application.Configuration;
using WarrantyBee.Application.Contracts.Identity;
using WarrantyBee.Application.Contracts.Users;
using WarrantyBee.Domain.Enums;
using WarrantyBee.Domain.Exceptions;

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
    private readonly IJobSchedulerClient _jobScheduler;

    /// <summary>
    /// Initializes a new instance of the <see cref="AuthService"/> class.
    /// </summary>
    /// <param name="config">Application configuration options.</param>
    /// <param name="tokenService">Service for generating and validating tokens.</param>
    /// <param name="cacheService">Service for caching data.</param>
    /// <param name="captchaService">Service for validating captchas.</param>
    /// <param name="otpService">Service for generating OTPs.</param>
    /// <param name="telemetryService">Service for logging and telemetry.</param>
    /// <param name="userRepository">Repository for user data.</param>
    /// <param name="otpRepository">Repository for OTP data.</param>
    /// <param name="jobScheduler">Client for scheduling background jobs.</param>
    public AuthService(
        IOptions<AppConfiguration> config,
        ITokenService tokenService,
        ICacheService cacheService,
        ICaptchaService captchaService,
        IOtpService otpService,
        ITelemetryService telemetryService,
        IUserRepository userRepository,
        IOtpRepository otpRepository,
        IJobSchedulerClient jobScheduler)
    {
        _config = config.Value;
        _tokenService = tokenService;
        _cacheService = cacheService;
        _captchaService = captchaService;
        _otpService = otpService;
        _telemetryService = telemetryService;
        _userRepository = userRepository;
        _otpRepository = otpRepository;
        _jobScheduler = jobScheduler;
    }

    /// <summary>
    /// Performs user login, supporting both simple and multi-factor authentication.
    /// </summary>
    /// <param name="request">The login request details.</param>
    /// <returns>A response indicating the result of the login attempt.</returns>
    /// <exception cref="ApiException">Thrown if captcha is invalid or request body is malformed.</exception>
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

    /// <summary>
    /// Registers a new user.
    /// </summary>
    /// <param name="request">The sign-up request details.</param>
    /// <returns>A response containing the ID of the newly created user.</returns>
    /// <exception cref="ApiException">Thrown if validation fails or registration cannot be completed.</exception>
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

        // Schedule Welcome Email Job immediately
        var macros = new Dictionary<string, string>
        {
            ["USER_FIRST_NAME"] = request.Firstname,
            ["USER_LAST_NAME"] = request.Lastname
        };
        await _jobScheduler.EnqueueNotificationAsync(request.Email, "WelcomeEmail", macros);

        return new SignUpResponse(userId);
    }

    /// <summary>
    /// Initiates the forgot password process by sending an OTP to the user's email.
    /// </summary>
    /// <param name="request">The forgot password request containing the user's email.</param>
    /// <returns>A task representing the asynchronous operation.</returns>
    /// <exception cref="ApiException">Thrown if email is invalid or password was recently updated.</exception>
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

    /// <summary>
    /// Resets the user's password using a valid OTP.
    /// </summary>
    /// <param name="request">The reset password request details.</param>
    /// <returns>A task representing the asynchronous operation.</returns>
    /// <exception cref="ApiException">Thrown if OTP is invalid, password does not meet requirements, or update fails.</exception>
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

        var macros = new Dictionary<string, string>
        {
            ["USER_FIRST_NAME"] = user.Firstname!,
            ["USER_LAST_NAME"] = user.Lastname!
        };
        await _jobScheduler.EnqueueNotificationAsync(user.Email!, "PasswordChanged", macros);
    }

    private async Task<ILoginResponse> ProcessSimpleLoginAsync(SimpleLoginRequest request)
    {
        ValidateSimpleLogin(request);

        var user = await _userRepository.GetAsync(new UserSearchFilter(null, request.Email));
        if (user == null) throw new ApiException(Errors.UserNotRegistered);

        if (request.AuthProvider == (byte)AuthProvider.Internal)
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

        return await GetLoginResponseAsync(user);
    }

    private async Task<LoginResponse> GetLoginResponseAsync(UserResponse user)
    {
        var cacheKey = $"token:{user.Email}";
        string? accessToken = null;

        try
        {
            accessToken = await _cacheService.GetAsync(cacheKey);
        }
        catch (Exception ex)
        {
            _telemetryService.Log(LogLevel.Warn, ex, new Dictionary<string, object> { ["Message"] = "Cache retrieval failed during login" });
        }

        if (accessToken == null)
        {
            var claims = new Dictionary<string, object>
            {
                ["userId"] = user.Id.ToString(),
                ["email"] = user.Email!,
                ["role"] = user.AuthorizationContext?.Role.ToString() ?? "CUSTOMER"
            };
            accessToken = _tokenService.Generate(claims);
            
            try
            {
                await _cacheService.SetAsync(cacheKey, accessToken, 3600);
            }
            catch (Exception ex)
            {
                _telemetryService.Log(LogLevel.Warn, ex, new Dictionary<string, object> { ["Message"] = "Cache storage failed during login" });
            }
        }

        var validatedClaims = _tokenService.Validate(accessToken);
        
        var iat = validatedClaims.ContainsKey("iat") ? validatedClaims["iat"].ToString()! : DateTime.UtcNow.ToString("O");
        var exp = validatedClaims.ContainsKey("exp") ? validatedClaims["exp"].ToString()! : DateTime.UtcNow.AddHours(1).ToString("O");

        return new LoginResponse(accessToken, iat, exp, user);
    }

    private async Task SendOtpAsync(long userId, string email, OtpRequestReason reason)
    {
        var otp = _otpService.Generate();
        var otpHash = HashHelper.GetHash(otp);
        await _otpRepository.StoreAsync(new OtpStorageRequest(otpHash, email, userId, (byte)reason));

        var macros = new Dictionary<string, string> 
        { 
            ["OTP"] = otp,
            ["EXPIRY_TIME"] = _config.Otp?.Expiration.ToString() ?? "5"
        };
        var templateName = reason == OtpRequestReason.Login ? "LoginOtp" : "ForgotPasswordOtp";
        
        await _jobScheduler.EnqueueNotificationAsync(email, templateName, macros);
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
        if (request.AuthProvider == (byte)AuthProvider.Internal && Validator.IsBlank(request.Password))
            throw new ApiException(Errors.PasswordRequired);
    }

    private void ValidateMfaLogin(MFALoginRequest request)
    {
        if (!Validator.IsEmail(request.Email)) throw new ApiException(Errors.InvalidEmail);
        if (Validator.IsBlank(request.Token)) throw new ApiException(Errors.TokenRequired);
        if (Validator.IsBlank(request.Otp)) throw new ApiException(Errors.OtpRequired);
    }
}
