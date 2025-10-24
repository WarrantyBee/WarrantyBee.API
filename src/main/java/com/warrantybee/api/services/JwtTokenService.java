package com.warrantybee.api.services;

import com.warrantybee.api.configurations.AppConfiguration;
import com.warrantybee.api.services.interfaces.ITokenService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
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
            throw new IllegalStateException("JWT secret is not configured.");
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

        DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT.withZone(ZoneOffset.UTC);
        System.out.println("IssuedAt UTC: " + formatter.format(nowUtc));
        System.out.println("ExpiresAt UTC: " + formatter.format(expiryUtc));

        return builder.sign(algorithm);
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> validate(String token) throws JWTVerificationException {
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
    }
}
