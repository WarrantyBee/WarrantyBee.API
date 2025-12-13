package com.warrantybee.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a simple login request that uses only email and password for authentication.
 */
@Getter
@Setter
@AllArgsConstructor
public class SimpleLoginRequest extends LoginRequest {
    /** The user's plain-text password. */
    private String password;

    /** Indicates which authentication provider was used (e.g., local, Google, Facebook). */
    private Byte authProvider;

    /** Stores the OAuth authorization code received from the authentication provider after user consent. */
    private String authCode;
}
