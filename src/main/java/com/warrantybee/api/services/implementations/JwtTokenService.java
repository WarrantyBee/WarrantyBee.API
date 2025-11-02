package com.warrantybee.api.services.implementations;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.warrantybee.api.configurations.AppConfiguration;
import com.warrantybee.api.exceptions.ConfigurationException;
import com.warrantybee.api.exceptions.InvalidTokenException;
import com.warrantybee.api.exceptions.JwtGenerationException;
import com.warrantybee.api.services.interfaces.ITokenService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Service to generate and validate JWT tokens using AppConfiguration.
 */
@Service
public class JwtTokenService implements ITokenService {

    private final Algorithm _algorithm;
    private final JWTVerifier _verifier;
    private final long _expirationMs;
    private final String _issuer;
    private final String _audience;

    /**
     * Initializes JWT service using AppConfiguration.
     *
     * @param appConfiguration application properties
     */
    public JwtTokenService(AppConfiguration appConfiguration) {
        var jwtConfig = appConfiguration.getJwtTokenConfiguration();

        if (jwtConfig.getSecret() == null || jwtConfig.getSecret().isEmpty()) {
            throw new ConfigurationException("JWT secret is not configured.");
        }

        this._algorithm = Algorithm.HMAC256(jwtConfig.getSecret());
        this._issuer = jwtConfig.getIssuer();
        this._audience = jwtConfig.getAudience();
        this._expirationMs = jwtConfig.getExpiration() * 60_000L;

        this._verifier = JWT.require(_algorithm)
                .withIssuer(_issuer)
                .withAudience(_audience)
                .build();
    }

    @Override
    public String generate(Map<String, Object> claims) {
        try {
            Instant nowUtc = Instant.now();
            Instant expiryUtc = nowUtc.plusMillis(_expirationMs);

            Date now = Date.from(nowUtc);
            Date expiry = Date.from(expiryUtc);

            var builder = JWT.create()
                    .withIssuer(_issuer)
                    .withAudience(_audience)
                    .withIssuedAt(now)
                    .withExpiresAt(expiry);

            claims.forEach((k, v) -> builder.withClaim(k, v.toString()));

            return builder.sign(_algorithm);
        } catch (Exception e) {
            throw new JwtGenerationException("Could not generate JWT token", e);
        }
    }

    @Override
    public Map<String, Object> validate(String token) {
        try {
            DecodedJWT decoded = _verifier.verify(token);
            Map<String, Object> claims = new HashMap<>();

            decoded.getClaims().forEach((k, v) -> claims.put(k, v.asString()));

            if (decoded.getIssuedAt() != null) {
                claims.put("iat", decoded.getIssuedAt().getTime());
            }
            if (decoded.getExpiresAt() != null) {
                claims.put("exp", decoded.getExpiresAt().getTime());
            }

            return claims;
        } catch (JWTVerificationException e) {
            throw new InvalidTokenException("Invalid JWT token", e);
        }
    }
}