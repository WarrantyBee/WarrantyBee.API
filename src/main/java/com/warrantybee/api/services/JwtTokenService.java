package com.warrantybee.api.services;

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

    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final long expirationMs;
    private final String issuer;
    private final String audience;

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

        this.algorithm = Algorithm.HMAC256(jwtConfig.getSecret());
        this.issuer = jwtConfig.getIssuer();
        this.audience = jwtConfig.getAudience();
        this.expirationMs = jwtConfig.getExpiration() * 60_000L;

        this.verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .withAudience(audience)
                .build();
    }

    /** {@inheritDoc} */
    @Override
    public String generate(Map<String, Object> claims) {
        try {
            Instant nowUtc = Instant.now();
            Instant expiryUtc = nowUtc.plusMillis(expirationMs);

            Date now = Date.from(nowUtc);
            Date expiry = Date.from(expiryUtc);

            var builder = JWT.create()
                    .withIssuer(issuer)
                    .withAudience(audience)
                    .withIssuedAt(now)
                    .withExpiresAt(expiry);

            claims.forEach((k, v) -> builder.withClaim(k, v.toString()));

            return builder.sign(algorithm);
        } catch (Exception e) {
            throw new JwtGenerationException("Could not generate JWT token", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> validate(String token) {
        try {
            DecodedJWT decoded = verifier.verify(token);
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