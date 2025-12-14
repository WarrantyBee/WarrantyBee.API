package com.warrantybee.api.dto.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.warrantybee.api.enumerations.AuthProvider;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a simple login request that uses only email and password for authentication.
 */
@Getter
@Setter
public class SimpleLoginRequest extends LoginRequest {
    /** The user's plain-text password. */
    private String password;

    /** Indicates which authentication provider was used (e.g., local, Google, Facebook). */
    private Byte authProvider;

    /** Unique user identifier provided by the external authentication provider. */
    private String authProviderUserId;
}
