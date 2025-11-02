package com.warrantybee.api.services.implementations;

import com.warrantybee.api.constants.EmailTemplate;
import com.warrantybee.api.dto.internal.EmailPayload;
import com.warrantybee.api.dto.internal.UserCreationRequest;
import com.warrantybee.api.dto.internal.UserSearchFilter;
import com.warrantybee.api.dto.request.LoginRequest;
import com.warrantybee.api.dto.request.SignUpRequest;
import com.warrantybee.api.dto.response.LoginResponse;
import com.warrantybee.api.dto.response.SignUpResponse;
import com.warrantybee.api.dto.response.UserResponse;
import com.warrantybee.api.exceptions.*;
import com.warrantybee.api.models.User;
import com.warrantybee.api.repositories.DBContext;
import com.warrantybee.api.repositories.interfaces.IUserRepository;
import com.warrantybee.api.services.interfaces.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Service responsible for handling authentication-related operations
 * such as user login, token generation, caching, and CAPTCHA validation.
 *
 * <p>This service integrates with token, cache, telemetry, and CAPTCHA services
 * to provide a secure login flow.</p>
 */
@Service
public class AuthService implements IAuthService {

    private final ITokenService _tokenService;
    private final ICacheService _cacheService;
    private final ICaptchaService _captchaService;
    private final IUserRepository _userRepository;
    private final BCryptPasswordEncoder _passwordEncoder;

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
        this._passwordEncoder = new BCryptPasswordEncoder(12);
    }

    /**
     * Authenticates a user based on the provided login credentials and CAPTCHA.
     * Generates and caches a JWT token upon successful authentication.
     *
     * @param request the login request containing user credentials and CAPTCHA response
     * @return a {@link LoginResponse} containing the JWT token and user details
     * @throws UserNotFoundException       if the user does not exist
     * @throws InvalidCredentialsException if the password is incorrect
     * @throws InvalidTokenException       if a cached token is invalid
     * @throws CacheException              if an error occurs while accessing the cache
     * @throws JwtGenerationException      if token generation fails
     */
    @Override
    public LoginResponse login(LoginRequest request) throws UserNotFoundException, InvalidCredentialsException,
            InvalidTokenException, CacheException, JwtGenerationException {
        boolean hasValidCaptcha = _captchaService.validate(request.getCaptchaResponse());

        if (hasValidCaptcha) {
            UserSearchFilter searchFilter = new UserSearchFilter(null, request.getEmail());
            User user = _userRepository.get(searchFilter);

            if (user != null) {
                if (!_passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                    throw new InvalidCredentialsException();
                }

                String token = _getCachedToken(user);
                if (token == null) {
                    token = _generateToken(user);
                    _cacheToken(user, token);
                }

                Map<String, Object> claims = _tokenService.validate(token);
                UserResponse userResponse = new UserResponse(user.getId(), user.getFirstname(), user.getLastname(), user.getEmail());

                return new LoginResponse(token, claims.get("iat").toString(), claims.get("exp").toString(), userResponse);
            } else {
                throw new UserNotRegisteredException();
            }
        } else {
            throw new InvalidCaptchaException();
        }
    }

    /**
     * Retrieves a valid cached token for the specified us'er if available.
     *
     * @param user the user whose cached token is requested
     * @return the cached JWT token, or {@code null} if none exists or is invalid
     */
    private String _getCachedToken(User user) throws CacheException, InvalidTokenException {
        String cacheKey = "token:" + user.getEmail();
        String cachedToken = _cacheService.get(cacheKey);
        if (cachedToken != null) {
            _tokenService.validate(cachedToken);
            return cachedToken;
        }
        return null;
    }

    /**
     * Generates a new JWT token for the specified user.
     *
     * @param user the user for whom the token is generated
     * @return the generated JWT token
     */
    private String _generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("userId", user.getId().toString());
        return _tokenService.generate(claims);
    }

    /**
     * Caches the given token for the specified user.
     *
     * @param user  the user associated with the token
     * @param token the token to cache
     */
    private void _cacheToken(User user, String token) throws CacheException {
        String cacheKey = "token:" + user.getEmail();
        _cacheService.set(cacheKey, token, 3600);
    }


    @Override
    public SignUpResponse signUp(SignUpRequest request) throws Exception {
        var hasValidCaptcha = _captchaService.validate(request.getCaptchaResponse());

        if (hasValidCaptcha) {
            UserSearchFilter filter = new UserSearchFilter(null, request.getEmail());
            User user = _userRepository.get(filter);

            if (user == null) {
                String encodedPassword = _passwordEncoder.encode(request.getPassword());

                UserCreationRequest userRequest = new UserCreationRequest();
                userRequest.setFirstname(request.getFirstname());
                userRequest.setLastname(request.getLastname());
                userRequest.setEmail(request.getEmail());
                userRequest.setPassword(encodedPassword);
                userRequest.setGender((byte) request.getGender().getCode());
                userRequest.setDob(request.getDob());
                userRequest.setAddressLine1(request.getAddressLine1());
                userRequest.setAddressLine2(request.getAddressLine2());
                userRequest.setCity(request.getCity());
                userRequest.setRegionId(request.getRegionId());
                userRequest.setCountryId(request.getCountryId());
                userRequest.setPostalCode(request.getPostalCode());
                userRequest.setAvatarUrl(request.getAvatarUrl());
                Long userId = _userRepository.create(userRequest);

                return new SignUpResponse(userId);
            }
            else {
                throw new UserAlreadyRegisteredException();
            }
        }
        else {
            throw new InvalidCaptchaException();
        }
    }


    @Override
    public void sendOtp(Long userId, String email) {
        User user;
         if (userId != null) {
            user = _userRepository.findById(userId);
            user = _userRepository.get(new UserSearchFilter(userId, null));
        } else if (email != null) {
            user = _userRepository.get(new UserSearchFilter(null, email));
        } else {
            throw new InvalidInputException("Either userId or email must be provided.");
         }
 
        if (email != null) {
            emailId = _userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException();
         }
 
        String otp = _otpService.generateOtp(user.getEmail());

        Map<String, String> macros = new HashMap<>();
        macros.put("name", user.getFirstname());
        macros.put("otp", otp);

        String emailBody = _templateService.process(EmailTemplate.OTP, macros);

        EmailPayload emailPayload = new EmailPayload();
        emailPayload.setTo(new String[]{user.getEmail()});
        emailPayload.setSubject("Your OTP for WarrantyBee");
        emailPayload.setBody(emailBody);

        _emailService.sendEmail(emailPayload);

        String cacheKey = "otp:" + user.getEmail();
        _cacheService.set(cacheKey, otp, 300);
    }

}

