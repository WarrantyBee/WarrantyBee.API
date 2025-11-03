package com.warrantybee.api.services.implementations;

import com.warrantybee.api.dto.internal.UserCreationRequest;
import com.warrantybee.api.dto.internal.UserSearchFilter;
import com.warrantybee.api.dto.request.LoginRequest;
import com.warrantybee.api.dto.request.OtpRequest;
import com.warrantybee.api.dto.request.SignUpRequest;
import com.warrantybee.api.dto.response.LoginResponse;
import com.warrantybee.api.dto.response.SignUpResponse;
import com.warrantybee.api.dto.response.UserResponse;
import com.warrantybee.api.enumerations.Gender;
import com.warrantybee.api.exceptions.*;
import com.warrantybee.api.helpers.PasswordHelper;
import com.warrantybee.api.helpers.Validator;
import com.warrantybee.api.models.User;
import com.warrantybee.api.repositories.interfaces.IUserRepository;
import com.warrantybee.api.services.interfaces.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
    private final IUserRepository _userRepository;

    @PersistenceContext
    private EntityManager _entityManager;

    /**
     * Constructs a new {@code AuthService} with the required dependencies.
     *
     * @param tokenService      the service responsible for JWT operations
     * @param cacheService      the service used for caching tokens
     * @param captchaService    the service used to validate CAPTCHA responses
     * @param userRepository    the repository used to manage user data
     */
    @Autowired
    public AuthService(ITokenService tokenService, ICacheService cacheService, ICaptchaService captchaService,
                       IUserRepository userRepository) {
        this._tokenService = tokenService;
        this._cacheService = cacheService;
        this._captchaService = captchaService;
        this._userRepository = userRepository;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        _validate(request);
        boolean hasValidCaptcha = _captchaService.validate(request.getCaptchaResponse());

        if (hasValidCaptcha) {
            UserSearchFilter searchFilter = new UserSearchFilter(null, request.getEmail());
            UserResponse user = _userRepository.get(searchFilter);

            if (user != null) {
                if (PasswordHelper.verify(request.getPassword(), user.getPassword())) {
                    throw new InvalidCredentialsException();
                }

                String token = _getAccessTokenFromCache(user.getEmail());
                if (token == null) {
                    token = _generateAccessToken(user);
                    _cacheToken(user.getEmail(), token);
                }

                Map<String, Object> claims = _tokenService.validate(token);
                return new LoginResponse(token, claims.get("iat").toString(), claims.get("exp").toString(), user);
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
    public SignUpResponse signUp(SignUpRequest request) {
        _validate(request);
        boolean hasValidCaptcha = _captchaService.validate(request.getCaptchaResponse());

        if (hasValidCaptcha) {
            UserSearchFilter filter = new UserSearchFilter(null, request.getEmail());
            UserResponse user = _userRepository.get(filter);
            String passwordHash = PasswordHelper.generate(request.getPassword());

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
    public void sendOtp(OtpRequest request) {
        _validate(request);
        String sender = request.getEmail();

        if (request.getUserId() == null) {
            UserSearchFilter filter = new UserSearchFilter(request.getUserId(), null);
            UserResponse user = _userRepository.get(filter);
            if (user == null) {
                throw new UserNotFoundException();
            }
            else {
                sender = user.getEmail();
            }
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
     * Validates the given OTP request.
     * @param request the OTP request to validate
     */
    private void _validate(OtpRequest request) {
        if (request == null) {
            throw new RequestBodyEmptyException();
        }
        else {
            if (request.getUserId() == null && Validator.isBlank(request.getEmail())) {
                throw new OtpReceiverRequiredException();
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
}

