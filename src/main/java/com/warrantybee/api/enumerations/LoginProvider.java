package com.warrantybee.api.enumerations;

import lombok.Getter;

/**
 * Represents the supported login providers within the WarrantyBee application.
 */
@Getter
public enum LoginProvider {

    /**
     * Indicates that no login provider is selected or applicable.
     */
    NONE((byte) 0),

    /**
     * Internal application-based authentication handled by WarrantyBee.
     */
    INTERNAL((byte) 1),

    /**
     * Authentication provided through Meta platforms such as Facebook.
     */
    FACEBOOK((byte) 2),

    /**
     * Authentication using Google OAuth services.
     */
    GOOGLE((byte) 3),

    /**
     * Authentication using LinkedIn OAuth services.
     */
    LINKEDIN((byte) 4);

    /**
     * Unique integer representation of the login provider.
     */
    private final Byte code;

    public Byte getCode() {
        return code;
    }

    /**
     * Creates a LoginProvider with its associated code.
     *
     * @param code the unique code representing the provider
     */
    LoginProvider(Byte code) {
        this.code = code;
    }
}
