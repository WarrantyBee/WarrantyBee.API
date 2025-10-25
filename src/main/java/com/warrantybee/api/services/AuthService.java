package com.warrantybee.api.services;

import com.warrantybee.api.dto.request.LoginRequest;
import com.warrantybee.api.dto.response.LoginResponse;
import com.warrantybee.api.dto.response.UserResponse;
import com.warrantybee.api.exceptions.*;
import com.warrantybee.api.models.User;
import com.warrantybee.api.services.interfaces.IAuthService;
import com.warrantybee.api.services.interfaces.ICacheService;
import com.warrantybee.api.services.interfaces.ITelemetryService;
import com.warrantybee.api.services.interfaces.ITokenService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService implements IAuthService {

    private final ITokenService _tokenService;
    private final ICacheService _cacheService;
    private final ITelemetryService _telemetryService;
    private final BCryptPasswordEncoder _passwordEncoder;

    @PersistenceContext
    private EntityManager _entityManager;

    @Autowired
    public AuthService(ITokenService tokenService, ICacheService cacheService,
                       ITelemetryService telemetryService) {
        this._tokenService = tokenService;
        this._cacheService = cacheService;
        this._telemetryService = telemetryService;
        this._passwordEncoder = new BCryptPasswordEncoder(12);
    }

    @Override
    public LoginResponse login(LoginRequest request) throws UserNotFoundException, InvalidCredentialsException, InvalidTokenException, CacheException, JwtGenerationException {
        User user = _fetchUserByEmail(request.getEmail());

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
    }

    private User _fetchUserByEmail(String email) {
        try {
            return _entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new UserNotFoundException();
        }
    }

    private String _getCachedToken(User user) {
        try {
            String cachedToken = _cacheService.get("token:" + user.getEmail());
            if (cachedToken != null) {
                _tokenService.validate(cachedToken);
                return cachedToken;
            }
        } catch (CacheException | InvalidTokenException e) {
            _telemetryService.logError(e, Map.of("user", user.getEmail()));
        }
        return null;
    }

    private String _generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("userId", user.getId().toString());
        return _tokenService.generate(claims);
    }

    private void _cacheToken(User user, String token) {
        try {
            _cacheService.set("token:" + user.getEmail(), token, 3600);
        } catch (CacheException e) {
            _telemetryService.logError(e, Map.of("user", user.getEmail()));
        }
    }
}