package com.warrantybee.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** DTO for login response */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    /** JWT access token */
    private String accessToken;

    /** Token issued timestamp */
    private String issuedAt;

    /** Token expiration timestamp */
    private String expiresAt;

    /** Logged-in user details */
    private UserResponse user;
}
