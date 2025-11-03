package com.warrantybee.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) representing the response returned upon successful user login.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    /**
     * The JSON Web Token (JWT) used for subsequent authenticated requests to protected API endpoints.
     */
    private String accessToken;

    /**
     * The timestamp (in ISO 8601 format) indicating the moment the access token was issued (iat claim).
     */
    private String issuedAt;

    /**
     * The timestamp (in ISO 8601 format) indicating the moment the access token will expire (exp claim).
     */
    private String expiresAt;

    /**
     * Details of the successfully logged-in user, typically containing the user ID, name, and roles.
     * @see UserResponse
     */
    private UserResponse user;
}
