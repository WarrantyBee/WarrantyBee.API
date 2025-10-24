package com.warrantybee.api.configurations;

import lombok.Getter;
import lombok.Setter;

/**
 * Holds configuration details for JWT generation and validation.
 */
@Getter
@Setter
public class JwtTokenConfiguration {

    /** The issuer of the JWT token. */
    private String issuer;

    /** The intended audience for the JWT token. */
    private String audience;

    /** The secret key used to sign the JWT token. */
    private String secret;

    /** The expiration time (in minutes) for the JWT token. */
    private int expiration;
}
