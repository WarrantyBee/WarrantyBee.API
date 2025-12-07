package com.warrantybee.api.enumerations;

import com.warrantybee.api.enumerations.interfaces.IEnumeration;
import lombok.Getter;

/**
 * Represents the supported login providers within the WarrantyBee application.
 */
@Getter
public enum LoginProvider implements IEnumeration {

    /**
     * Indicates that no login provider is selected or applicable.
     */
    NONE(0),

    /**
     * Internal application-based authentication handled by WarrantyBee.
     */
    INTERNAL(1),

    /**
     * Authentication provided through Meta platforms such as Facebook.
     */
    FACEBOOK(2),

    /**
     * Authentication using Google OAuth services.
     */
    GOOGLE(3),

    /**
     * Authentication using LinkedIn OAuth services.
     */
    LINKEDIN(4);

    /**
     * Unique integer representation of the login provider.
     */
    private final int code;

    /**
     * Creates a LoginProvider with its associated code.
     *
     * @param code the unique code representing the provider
     */
    LoginProvider(int code) {
        this.code = code;
    }
}
