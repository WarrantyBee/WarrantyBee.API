package com.warrantybee.api.services.implementations;

import com.warrantybee.api.dto.internal.*;
import com.warrantybee.api.dto.request.*;
import com.warrantybee.api.dto.request.interfaces.ILoginRequest;
import com.warrantybee.api.dto.response.LoginResponse;
import com.warrantybee.api.dto.response.MFALoginResponse;
import com.warrantybee.api.dto.response.SignUpResponse;
import com.warrantybee.api.dto.response.UserResponse;
import com.warrantybee.api.dto.response.interfaces.ILoginResponse;
import com.warrantybee.api.enumerations.Gender;
import com.warrantybee.api.enumerations.LogLevel;
import com.warrantybee.api.enumerations.OtpRequestReason;
import com.warrantybee.api.exceptions.*;
import com.warrantybee.api.helpers.HashHelper;
import com.warrantybee.api.helpers.Validator;
import com.warrantybee.api.repositories.interfaces.IOtpRepository;
import com.warrantybee.api.repositories.interfaces.IUserRepository;
import com.warrantybee.api.services.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Service responsible for handling authentication-related operations.
 */
@Service
public class AuthService implements IAuthService {

    private final ITokenService _tokenService;
    private final ICacheService _cacheService;
    private final ICaptchaService _captchaService;
    private final IOtpService _otpService;
    private final IEmailService _emailService;
    private final ITelemetryService _telemetryService;
    private final IUserRepository _userRepository;
    private final IOtpRepository _otpRepository;

    /**
     * Constructs a new {@code AuthService} with the required dependencies.
     *
     * @param tokenService      the service responsible for JWT operations
     * @param cacheService      the service used for caching tokens
     * @param captchaService    the service used to validate CAPTCHA responses
     * @param emailService      the service used to send emails
     * @param userRepository    the repository used to manage user data
     * @param otpRepository     tge repository used to store and retrieve OTPs
     */
    @Autowired
    public AuthService(ITokenService tokenService, ICacheService cacheService, ICaptchaService captchaService,
                       IOtpService otpService, IEmailService emailService, ITelemetryService telemetryService,
                       IUserRepository userRepository, IOtpRepository otpRepository) {
        this._tokenService = tokenService;
        this._cacheService = cacheService;
        this._captchaService = captchaService;
        this._otpService = otpService;
        this._emailService = emailService;
        this._telemetryService = telemetryService;
        this._userRepository = userRepository;
        this._otpRepository = otpRepository;
    }

    @Override
    public ILoginResponse login(ILoginRequest request) {
        LoginRequest loginRequest = null;
        MFALoginRequest mfaLoginRequest = null;
        boolean hasValidCaptcha = false;
        ILoginResponse response = null;

        if (request instanceof LoginRequest obj) {
            loginRequest = obj;
        }
        if (request instanceof MFALoginRequest obj) {
            mfaLoginRequest = obj;
        }

        if (loginRequest != null) {
            hasValidCaptcha = _captchaService.validate(loginRequest.getCaptchaResponse());
            if (hasValidCaptcha) {
                response = _process(loginRequest);
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
            String passwordHash = HashHelper.getHash(request.getPassword());

            if (user == null) {
                UserCreationRequest userCreationRequest = new UserCreationRequest(
                    request.getFirstname(),
                    request.getLastname(),
                    request.getEmail(),
                    passwordHash,
                    request.getGender(),
                    request.getDateOfBirth(),
                    request.getPhoneNumber(),
                    request.getAddressLine1(),
                    request.getAddressLine2(),
                    request.getCity(),
                    request.getRegionId(),
                    request.getCountryId(),
                    request.getPostalCode(),
                    request.getAvatarUrl()
                );
                Long userId = _userRepository.create(userCreationRequest);

                if (userId == null) {
                    throw new UserRegistrationFailedException();
                }

                try {
                    _emailService.sendWelcomeMail(request);
                }
                catch (Exception e) {
                    Map<String, Object> context = new HashMap<>();
                    context.put("exception", e);
                    _telemetryService.log(LogLevel.WARN, "A failure happened while sending the welcome email.", context);
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
            OtpRequest otpRequest = new OtpRequest(null, request.getEmail(), OtpRequestReason.ForgotPassword);
            _sendOtp(otpRequest);
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
                OtpSearchFilter otpSearchFilter = new OtpSearchFilter(user.getEmail(), user.getId(), (byte) OtpRequestReason.ForgotPassword.getCode());
                String otpHash = _otpRepository.get(otpSearchFilter);

                if (!HashHelper.verify(request.getOtp(), otpHash)) {
                    throw new InvalidOtpException();
                }
                else {
                    String passwordHash = HashHelper.getHash(request.getNewPassword());
                    PasswordResetRequest resetRequest = new PasswordResetRequest(user.getId(), passwordHash);
                    Boolean success = _userRepository.resetPassword(resetRequest);
                    if (!success) {
                        throw new PasswordResetFailedException();
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
     * Validates the given login request.
     * @param request the login request to validate
     */
    private void _validate(LoginRequest request) {
        if (request == null) {
            throw new RequestBodyEmptyException();
        }
        else {
            if (Validator.isBlank(request.getCaptchaResponse())) {
                throw new CaptchaResponseRequiredException();
            }
            if (Validator.isBlank(request.getEmail())) {
                throw new EmailRequiredException();
            }
            if (Validator.isBlank(request.getPassword())) {
                throw new PasswordRequiredException();
            }
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
            if (Validator.isBlank(request.getCaptchaResponse())) {
                throw new CaptchaResponseRequiredException();
            }
            if (Validator.isBlank(request.getEmail())) {
                throw new EmailRequiredException();
            }
            if (Validator.isBlank(request.getPassword())) {
                throw new PasswordRequiredException();
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
     * Validates the given sign-up request for required and valid user details.
     * @param request the sign-up request to validate
     */
    private void _validate(SignUpRequest request) {
        if (request == null) {
            throw new RequestBodyEmptyException();
        }
        else {
            if (Validator.isBlank(request.getCaptchaResponse())) {
                throw new CaptchaResponseRequiredException();
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
            if (Validator.isBlank(request.getPassword())) {
                throw new PasswordRequiredException();
            }
            if (!Validator.isStrongPassword(request.getPassword())) {
                throw new StrongPasswordRequiredException();
            }
            if (!Validator.isEnum(request.getGender(), Gender.class)) {
                throw new InvalidGenderValueException();
            }
            if (!Validator.hasLegalAge(request.getDateOfBirth())) {
                throw new UserIsMinorException();
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
    private ILoginResponse _process(LoginRequest request) {
        _validate(request);

        UserSearchFilter searchFilter = new UserSearchFilter(null, request.getEmail());
        UserResponse user = _userRepository.get(searchFilter);

        if (user != null) {
            if (!HashHelper.verify(request.getPassword(), user.getPassword())) {
                throw new InvalidCredentialsException();
            }

            if (user.getProfile().getSettings().getIs2FAEnabled()) {
                LoginTokenDetails token = new LoginTokenDetails(user.getId(), HashHelper.generateToken());
                Boolean isStored = _userRepository.store(token);

                if (isStored) {
                    OtpRequest otpRequest = new OtpRequest(user.getId(), user.getEmail(), OtpRequestReason.Login);
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
            if (!HashHelper.verify(request.getPassword(), user.getPassword())) {
                throw new InvalidCredentialsException();
            }

            if (user.getProfile().getSettings().getIs2FAEnabled()) {
                LoginTokenDetails token = new LoginTokenDetails(user.getId(), request.getToken());
                Boolean isValid = _userRepository.validate(token);

                if (isValid) {
                    OtpSearchFilter filter = new OtpSearchFilter(user.getEmail(), user.getId(), (byte) OtpRequestReason.Login.getCode());
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
     * Generates and sends an OTP to the specified user based on the request details.
     * @param request the OTP request containing the user's email, ID, and reason
     */
    private void _sendOtp(OtpRequest request) {
        String recipient = request.getEmail();
        OtpRequestReason reason = request.getReason();
        Map<String, String> macros = new HashMap<>();

        if (reason == OtpRequestReason.Login) {
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
            OtpEmailPayload payload = new OtpEmailPayload(recipient, otp, reason, macros);
            _emailService.sendOtp(payload);
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
