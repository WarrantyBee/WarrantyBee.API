package com.warrantybee.api.services.interfaces;

import com.warrantybee.api.exceptions.InvalidTokenException;
import com.warrantybee.api.exceptions.JwtGenerationException;

import java.util.Map;

/**
 * Interface for JWT token operations such as generation and validation.
 */
public interface ITokenService {

    /**
     * Generates a JWT token with the specified claims.
     *
     * @param claims a map of claims to include in the token
     * @return the generated JWT token as a string
     * @throws JwtGenerationException if the token generation fails
     */
    String generate(Map<String, Object> claims) throws JwtGenerationException;

    /**
     * Validates a JWT token and returns the claims if valid.
     *
     * @param token the JWT token to validate
     * @return a map of claims if the token is valid
     * @throws InvalidTokenException if the token is invalid or expired
     */
    Map<String, Object> validate(String token) throws InvalidTokenException;
}