package com.warrantybee.api.services.implementations;

import com.warrantybee.api.configurations.AppConfiguration;
import com.warrantybee.api.dto.internal.*;
import com.warrantybee.api.dto.request.*;
import com.warrantybee.api.dto.request.LoginRequest;
import com.warrantybee.api.dto.response.*;
import com.warrantybee.api.dto.response.interfaces.ILoginResponse;
import com.warrantybee.api.enumerations.*;
import com.warrantybee.api.exceptions.*;
import com.warrantybee.api.helpers.HashHelper;
import com.warrantybee.api.helpers.Validator;
import com.warrantybee.api.repositories.interfaces.IOtpRepository;
import com.warrantybee.api.repositories.interfaces.IUserRepository;
import com.warrantybee.api.services.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Service responsible for handling authentication-related operations.
 */
@Service
public class AuthService implements IAuthService {

    private final AppConfiguration _appConfiguration;
    private final ITokenService _tokenService;
    private final ICacheService _cacheService;
    private final ICaptchaService _captchaService;
    private final IOtpService _otpService;
    private final IEmailService _emailService;
    private final ITelemetryService _telemetryService;
    private final IUserRepository _userRepository;
    private final IOtpRepository _otpRepository;
    private final CommonOAuthService _oAuthService;

    /**
     * Constructs a new {@code AuthService} instance with all required dependencies.
     *
     * @param appConfiguration   the global application configuration containing environment and service settings
     * @param tokenService       the service responsible for generating and validating JWT tokens
     * @param cacheService       the service used for managing cached authentication data
     * @param captchaService     the service used to validate CAPTCHA responses during login or signup
     * @param otpService         the service responsible for generating, sending, and validating OTPs
     * @param emailService       the service used to send authentication-related emails (e.g., OTP, password reset)
     * @param telemetryService   the service used to capture and log authentication telemetry data
     * @param userRepository     the repository used to access and manage user account data
     * @param otpRepository      the repository used to store and retrieve OTP records
     * @param oAuthService       the generic OAuthService which is integrated with multiple auth providers
     */
    @Autowired
    public AuthService(AppConfiguration appConfiguration, ITokenService tokenService, ICacheService cacheService,
                       ICaptchaService captchaService, IOtpService otpService, IEmailService emailService,
                       ITelemetryService telemetryService, IUserRepository userRepository, IOtpRepository otpRepository,
                       CommonOAuthService oAuthService) {
        this._appConfiguration = appConfiguration;
        this._tokenService = tokenService;
        this._cacheService = cacheService;
        this._captchaService = captchaService;
        this._otpService = otpService;
        this._emailService = emailService;
        this._telemetryService = telemetryService;
        this._userRepository = userRepository;
        this._otpRepository = otpRepository;
        this._oAuthService = oAuthService;
    }

    @Override
    public ILoginResponse login(LoginRequest request) {
        SimpleLoginRequest simpleLoginRequest = null;
        MFALoginRequest mfaLoginRequest = null;
        boolean hasValidCaptcha = false;
        ILoginResponse response = null;

        if (request instanceof SimpleLoginRequest obj) {
            simpleLoginRequest = obj;
        }
        if (request instanceof MFALoginRequest obj) {
            mfaLoginRequest = obj;
        }

        if (simpleLoginRequest != null) {
            hasValidCaptcha = _captchaService.validate(simpleLoginRequest.getCaptchaResponse());
            if (hasValidCaptcha) {
                response = _process(simpleLoginRequest);
            }
        }
        else if (mfaLoginRequest != null) {
            hasValidCaptcha = _captchaService.validate(mfaLoginRequest.getCaptchaResponse());
            if (hasValidCaptcha) {
                response = _process(mfaLoginRequest);
            }
        }
        else {
            throw new InvalidRequestBodyException();
        }

        if (!hasValidCaptcha) {
            throw new CaptchaVerificationFailedException();
        }

        return response;
    }

    @Override
    public SignUpResponse signUp(SignUpRequest request) {
        _validate(request);
        boolean hasValidCaptcha = _captchaService.validate(request.getCaptchaResponse());

        if (hasValidCaptcha) {
            UserSearchFilter filter = new UserSearchFilter(null, request.getEmail());
            UserResponse user = _userRepository.get(filter);
            String passwordHash = request.getAuthProvider() == AuthProvider.INTERNAL.getCode() ?
                    HashHelper.getHash(request.getPassword()) : null;
            request.setPassword(passwordHash);
            String authProviderUserId = request.getAuthProvider() == AuthProvider.INTERNAL.getCode() ?
                    null : HashHelper.getHash(request.getAuthProviderUserId());
            request.setAuthProviderUserId(authProviderUserId);

            if (user == null) {
                Long userId = _userRepository.create(request);

                if (userId == null) {
                    throw new UserRegistrationFailedException();
                }

                try {
                    Map<String, String> macros = new HashMap<>();
                    macros.put("USER_FIRST_NAME", request.getFirstname());
                    macros.put("USER_LAST_NAME", request.getLastname());
                    NotificationPayload notification = new NotificationPayload(request.getEmail(), macros, NotificationType.WELCOME);
                    _emailService.send(notification);
                }
                catch (Exception e) {
                    Map<String, Object> context = new HashMap<>();
                    context.put("exception", e);
                    _telemetryService.log(LogLevel.ERROR, "A failure happened while sending the welcome email.", context);
                }

                return new SignUpResponse(userId);
            }
            else {
                throw new UserAlreadyRegisteredException();
            }
        }
        else {
            throw new CaptchaVerificationFailedException();
        }
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        _validate(request);
        boolean hasValidCaptcha = _captchaService.validate(request.getCaptchaResponse());

        if (hasValidCaptcha) {
            UserSearchFilter filter = new UserSearchFilter(null, request.getEmail());
            UserResponse user = _userRepository.get(filter);
            if (user != null) {
                boolean hasPasswordResetWindow = true;
                Timestamp passwordUpdatedAt = user.getProfile().getSettings().getPasswordUpdatedAt();

                if (passwordUpdatedAt != null) {
                    Instant now = Instant.now();
                    Instant lastUpdatedAt = passwordUpdatedAt.toInstant();
                    Instant leastAllowedTime = now.minus(_appConfiguration.getProfileConfiguration().getPasswordResetWindow(), ChronoUnit.MINUTES);
                    hasPasswordResetWindow = !lastUpdatedAt.isAfter(leastAllowedTime);
                }

                if (hasPasswordResetWindow) {
                    OtpRequest otpRequest = new OtpRequest(user.getId(), request.getEmail(), OtpRequestReason.FORGOT_PASSWORD);
                    _sendOtp(otpRequest);
                }
                else {
                    throw new PasswordRecentlyUpdatedException();
                }
            }
            else {
                throw new UserNotRegisteredException();
            }
        }
        else {
            throw new CaptchaVerificationFailedException();
        }
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        _validate(request);
        boolean hasValidCaptcha = _captchaService.validate(request.getCaptchaResponse());

        if (hasValidCaptcha) {
            UserSearchFilter userSearchFilter = new UserSearchFilter(null, request.getEmail());
            UserResponse user = _userRepository.get(userSearchFilter);

            if (user != null) {
                OtpSearchFilter otpSearchFilter = new OtpSearchFilter(user.getEmail(), user.getId(), (byte) OtpRequestReason.FORGOT_PASSWORD.getCode());
                String otpHash = _otpRepository.get(otpSearchFilter);

                if (!HashHelper.verify(request.getOtp(), otpHash)) {
                    throw new InvalidOtpException();
                }
                else {
                    AtomicBoolean previouslyUsedPassword = new AtomicBoolean(false);
                    List<String> previousPasswords = _userRepository.getPasswords(user.getId());

                    previousPasswords.forEach(pwd -> {
                        previouslyUsedPassword.set(HashHelper.verify(request.getNewPassword(), pwd));
                    });

                    if (previouslyUsedPassword.get()) {
                        throw new PasswordAlreadyUsedException();
                    }

                    String passwordHash = HashHelper.getHash(request.getNewPassword());
                    PasswordResetRequest resetRequest = new PasswordResetRequest(user.getId(), passwordHash);
                    Boolean success = _userRepository.resetPassword(resetRequest);
                    if (!success) {
                        throw new PasswordResetFailedException();
                    }
                    else {
                        Map<String, String> macros = new HashMap<>();
                        macros.put("USER_FIRST_NAME", user.getFirstname());
                        macros.put("USER_LAST_NAME", user.getLastname());
                        NotificationPayload notification = new NotificationPayload(user.getEmail(), macros, NotificationType.PASSWORD_CHANGED);
                        _emailService.send(notification);
                    }
                }
            }
            else {
                throw new UserNotRegisteredException();
            }
        }
        else {
            throw new CaptchaVerificationFailedException();
        }
    }

    /**
     * Validates the given multifactor authentication login request.
     * @param request the multifactor authentication request
     */
    private void _validate(MFALoginRequest request) {
        if (request == null) {
            throw new RequestBodyEmptyException();
        }
        else {
            if (Validator.isBlank(request.getEmail())) {
                throw new EmailRequiredException();
            }
            if (!Validator.isEmail(request.getEmail())) {
                throw new InvalidEmailException();
            }
            if (Validator.isBlank(request.getToken())) {
                throw new TokenRequiredException();
            }
            if (Validator.isBlank(request.getOtp())) {
                throw new OtpRequiredException();
            }
        }
    }

    /**
     * Validates the given login request.
     * @param request the login request to validate
     */
    private void _validate(SimpleLoginRequest request) {
        if (request == null) {
            throw new RequestBodyEmptyException();
        }
        else {
            if (Validator.isBlank(request.getEmail())) {
                throw new EmailRequiredException();
            }
            if (!Validator.isEmail(request.getEmail())) {
                throw new InvalidEmailException();
            }
            if (Validator.isBlank(request.getPassword())) {
                throw new PasswordRequiredException();
            }

            AuthProvider provider = AuthProvider.getValue(request.getAuthProvider());
            if (provider == AuthProvider.NONE) {
                throw new AuthProviderNotSupportedException();
            }
            if (provider != AuthProvider.INTERNAL && Validator.isBlank(request.getAuthCode())) {
                throw new AuthorizationCodeRequired();
            }
        }
    }

    /**
     * Validates the given sign-up request for required and valid user details.
     * @param request the sign-up request to validate
     */
    private void _validate(SignUpRequest request) {
        if (request == null) {
            throw new RequestBodyEmptyException();
        }
        else {
            if (request.getHasAcceptedTermsAndConditions() == null ||
                !request.getHasAcceptedTermsAndConditions()) {
                throw new TermsAndConditionsAreNotAcceptedException();
            }
            if (request.getHasAcceptedPrivacyPolicy() == null ||
                !request.getHasAcceptedPrivacyPolicy()) {
                throw new PrivacyPolicyNotAcceptedException();
            }
            if (Validator.isBlank(request.getFirstname())) {
                throw new FirstnameRequiredException();
            }
            if (Validator.isBlank(request.getLastname())) {
                throw new LastnameRequiredException();
            }
            if (Validator.isBlank(request.getEmail())) {
                throw new EmailRequiredException();
            }
            if (!Validator.isEmail(request.getEmail())) {
                throw new InvalidEmailException();
            }
            AuthProvider provider = AuthProvider.getValue(request.getAuthProvider());
            if (provider == AuthProvider.NONE) {
                throw new AuthProviderNotSupportedException();
            }
            if (provider == AuthProvider.INTERNAL) {
                if (Validator.isBlank(request.getPassword())) {
                    throw new PasswordRequiredException();
                }
                if (!Validator.isStrongPassword(request.getPassword())) {
                    throw new StrongPasswordRequiredException();
                }
            }
            else {
                if (Validator.isBlank(request.getAuthProviderUserId())) {
                    throw new AuthProviderUserIdentifierRequiredException();
                }
                request.setPassword(null);
            }
            if (!Validator.isEnum(request.getGender(), Gender.class)) {
                throw new InvalidGenderValueException();
            }
            if (!Validator.hasLegalAge(request.getDateOfBirth())) {
                throw new UserIsMinorException();
            }
            if (Validator.isBlank(request.getPhoneCode())) {

            }
            if (!Validator.isPhoneCode(request.getPhoneCode())) {

            }
            if (Validator.isBlank(request.getPhoneNumber())) {
                throw new PhoneNumberRequiredException();
            }
            if (Validator.isBlank(request.getAddressLine1())) {
                throw new AddressRequiredException();
            }
            if (Validator.isBlank(request.getCity())) {
                throw new CityRequiredException();
            }
            if (Validator.isBlank(request.getPostalCode())) {
                throw new PostalCodeRequiredException();
            }
            if (request.getRegionId() == null) {
                throw new RegionRequiredException();
            }
            if (request.getCountryId() == null) {
                throw new CountryRequiredException();
            }
            if (request.getCultureId() == null) {
                throw new CultureRequiredException();
            }
        }
    }

    /**
     * Validates the forgot password request.
     */
    private void _validate(ForgotPasswordRequest request) {
        if (request == null) {
            throw new RequestBodyEmptyException();
        }
        else {
            if (Validator.isBlank(request.getEmail())) {
                throw new OtpRecipientRequiredException();
            }
            if (!Validator.isEmail(request.getEmail())) {
                throw new InvalidOtpRecipientException();
            }
        }
    }

    /**
     * Validates the reset password request.
     */
    private void _validate(ResetPasswordRequest request) {
        if (request == null) {
            throw new RequestBodyEmptyException();
        }
        else {
            if (Validator.isBlank(request.getOtp())) {
                throw new OtpRequiredException();
            }
            if (Validator.isBlank(request.getEmail())) {
                throw new EmailRequiredException();
            }
            if (!Validator.isEmail(request.getEmail())) {
                throw new InvalidOtpRecipientException();
            }
            if (Validator.isBlank(request.getNewPassword())) {
                throw new PasswordRequiredException();
            }
            if (!Validator.isStrongPassword(request.getNewPassword())) {
                throw new StrongPasswordRequiredException();
            }
        }
    }

    /**
     * Retrieves and validates an access token from the cache for the specified email.
     * @param email the user's email address used to generate the cache key
     * @return the valid cached access token, or {@code null} if no valid token exists
     */
    private String _getAccessTokenFromCache(String email) {
        String cacheKey = String.format("token:%s", email);
        String cachedToken = _cacheService.get(cacheKey);
        if (cachedToken != null) {
            _tokenService.validate(cachedToken);
            return cachedToken;
        }
        return null;
    }

    /**
     * Generates a new access token for the specified user with user ID and email as claims.
     * @param user the user details used to create token claims
     * @return the generated access token as a string
     */
    private String _generateAccessToken(UserResponse user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId().toString());
        claims.put("email", user.getEmail());
        claims.put("role", user.getAuthorizationContext().getRole().getName());
        claims.put("permissions",
            String.join(",",
                user.getAuthorizationContext()
                    .getPermissions()
                    .stream()
                    .map(SecurityPermission::getName)
                    .toList()
            )
        );
        return _tokenService.generate(claims);
    }

    /**
     * Caches the given token for the specified email with a 1-hour expiry.
     * @param email the user's email associated with the token
     * @param token the access token to be cached
     */
    private void _cacheToken(String email, String token) {
        String cacheKey = String.format("token:%s", email);
        _cacheService.set(cacheKey, token, 3600);
    }

    /**
     * Processes a standard login request by validating credentials and handling MFA if enabled.
     *
     * @param request The login request containing user email and password.
     * @return A {@link ILoginResponse} object, which can be a {@link LoginResponse} or {@link MFALoginResponse}.
     */
    private ILoginResponse _process(SimpleLoginRequest request) {
        _validate(request);

        UserSearchFilter searchFilter = new UserSearchFilter(null, request.getEmail());
        UserResponse user = _userRepository.get(searchFilter);

        if (user != null) {
            _validateCredentials(request, user);

            if (user.getProfile().getSettings().getIs2FAEnabled()) {
                LoginTokenDetails token = new LoginTokenDetails(user.getId(), HashHelper.generateToken());
                Boolean isStored = _userRepository.store(token);

                if (isStored) {
                    OtpRequest otpRequest = new OtpRequest(user.getId(), user.getEmail(), OtpRequestReason.LOGIN);
                    _sendOtp(otpRequest);
                    return new MFALoginResponse(token.getToken());
                }
                else {
                    throw new LoginTokenCouldNotBeSavedException();
                }
            }
            else {
                return _getLoginResponse(user);
            }
        }
        else {
            throw new UserNotRegisteredException();
        }
    }

    /**
     * Processes an MFA login request by validating credentials, verifying the login token and OTP,
     * and returning a valid {@link LoginResponse} upon successful authentication.
     *
     * @param request The MFA login request containing email, password, token, and OTP.
     * @return A {@link LoginResponse} if authentication succeeds.
     */
    private LoginResponse _process(MFALoginRequest request) {
        _validate(request);
        UserSearchFilter searchFilter = new UserSearchFilter(null, request.getEmail());
        UserResponse user = _userRepository.get(searchFilter);

        if (user != null) {
            if (user.getProfile().getSettings().getIs2FAEnabled()) {
                LoginTokenDetails token = new LoginTokenDetails(user.getId(), request.getToken());
                Boolean isValid = _userRepository.validate(token);

                if (isValid) {
                    OtpSearchFilter filter = new OtpSearchFilter(user.getEmail(), user.getId(), (byte) OtpRequestReason.LOGIN.getCode());
                    String otpHash = _otpRepository.get(filter);

                    if (!HashHelper.verify(request.getOtp(), otpHash)) {
                        throw new InvalidOtpException();
                    }
                    else {
                        return _getLoginResponse(user);
                    }
                }
                else {
                    throw new InvalidLoginTokenException();
                }
            }
            else {
                throw new MFANotEnabledException();
            }
        }
        else {
            throw new UserNotRegisteredException();
        }
    }

    /**
     * Validates the user's credentials based on the configured authentication provider.
     *
     * @param request the incoming login request
     * @param user the stored user details
     */
    private void _validateCredentials(SimpleLoginRequest request, UserResponse user) {
        AuthProvider requestedProvider = AuthProvider.getValue(request.getAuthProvider());
        AuthProvider provider = AuthProvider.getValue(user.getAuthProvider());

        if (provider == AuthProvider.NONE || requestedProvider != provider) {
            throw new AuthProviderNotConfiguredException();
        }

        if (provider == AuthProvider.INTERNAL) {
            if (!HashHelper.verify(request.getPassword(), user.getPassword())) {
                throw new InvalidCredentialsException();
            }
        }
        else {
            OAuthProfileRequest profileRequest = new OAuthProfileRequest(
                request.getAuthCode(),
                request.getAuthProvider(),
                (byte) OAuthCallback.SIGN_IN.getCode()
            );
            SocialUserProfileResponse socialProfile = _oAuthService.getProfile(profileRequest);
            if (!HashHelper.verify(socialProfile.getId(), user.getAuthProviderUserId())) {
                throw new InvalidCredentialsException();
            }
        }
    }

    /**
     * Generates and sends an OTP to the specified user based on the request details.
     * @param request the OTP request containing the user's email, ID, and reason
     */
    private void _sendOtp(OtpRequest request) {
        String recipient = request.getEmail();
        OtpRequestReason reason = request.getReason();
        Map<String, String> macros = new HashMap<>();

        if (reason == OtpRequestReason.LOGIN ||
            reason == OtpRequestReason.FORGOT_PASSWORD) {
            if (request.getUserId() != null) {
                UserSearchFilter filter = new UserSearchFilter(request.getUserId(), null);
                UserResponse user = _userRepository.get(filter);
                if (user == null) {
                    throw new OtpRecipientRequiredException();
                }
                else {
                    recipient = user.getEmail();
                    macros.put("USER_FIRST_NAME", user.getFirstname());
                    macros.put("USER_LAST_NAME", user.getLastname());
                }
            }
            else {
                throw new OtpRecipientRequiredException();
            }
        }

        final String otp = _otpService.generate();
        final String otpHash = HashHelper.getHash(otp);
        OtpStorageRequest otpStorageRequest = new OtpStorageRequest(otpHash, recipient, request.getUserId(), (byte) reason.getCode());
        Long otpId = _otpRepository.store(otpStorageRequest);

        if (otpId == null) {
            throw new OtpGenerationFailedException();
        }
        else {
            macros.put("OTP", otp);
            NotificationPayload notification = null;
            switch (reason) {
                case OtpRequestReason.LOGIN -> {
                    notification = new NotificationPayload(recipient, macros, NotificationType.MFA_LOGIN);
                }
                case OtpRequestReason.FORGOT_PASSWORD -> {
                    notification = new NotificationPayload(recipient, macros, NotificationType.FORGOT_PASSWORD);
                }
            }
            _emailService.send(notification);
        }
    }

    /**
     * Builds and returns a {@link LoginResponse} for the given user.
     * Retrieves the access token from cache if available, otherwise generates and stores a new one.
     *
     * @param user The authenticated user details.
     * @return A populated {@link LoginResponse} containing the token and related claims.
     */
    private LoginResponse _getLoginResponse(UserResponse user) {
        String accessToken = _getAccessTokenFromCache(user.getEmail());

        if (accessToken == null) {
            accessToken = _generateAccessToken(user);
            _cacheToken(user.getEmail(), accessToken);
        }

        Map<String, Object> claims = _tokenService.validate(accessToken);
        return new LoginResponse(
            accessToken,
            claims.get("iat").toString(),
            claims.get("exp").toString(),
            user
        );
    }
}
