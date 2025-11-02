package com.warrantybee.api.configurations;

import lombok.Getter;
import lombok.Setter;

/**
 * Configuration class for defining properties related to the creation
 * and validation of JSON Web Tokens (JWT).
 */
@Getter
@Setter
public class JwtTokenConfiguration {

    /** The issuer claim (iss) for the JWT token. */
    private String issuer;

    /** The intended audience claim (aud) for the JWT token. */
    private String audience;

    /** The secret key used for signing and verifying the JWT token integrity. */
    private String secret;

    /** The token expiration time, specified in minutes. */
    private int expiration;
}
