package com.warrantybee.api.services.implementations;
import com.warrantybee.api.dto.request.LoginRequest;
import com.warrantybee.api.dto.response.LoginResponse;
import com.warrantybee.api.dto.response.UserResponse;
import com.warrantybee.api.exceptions.*;
import com.warrantybee.api.models.User;
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
            User user = _userRepository.findByEmail(request.getEmail());

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
     * Retrieves a valid cached token for the specified user if available.
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
}
