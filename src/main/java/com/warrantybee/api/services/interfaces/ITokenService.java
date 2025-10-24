package com.warrantybee.api.services.interfaces;

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
     */
    String generate(Map<String, Object> claims);

    /**
     * Validates a JWT token and returns the claims if valid.
     *
     * @param token the JWT token to validate
     * @return a map of claims if the token is valid
     * @throws Exception if the token is invalid or expired
     */
    Map<String, Object> validate(String token) throws Exception;
}
