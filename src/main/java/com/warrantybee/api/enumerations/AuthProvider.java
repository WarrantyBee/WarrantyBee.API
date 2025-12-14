package com.warrantybee.api.enumerations;

import com.warrantybee.api.enumerations.interfaces.IEnumeration;
import lombok.Getter;

/**
 * Defines the supported authentication providers used in the WarrantyBee application.
 */
@Getter
public enum AuthProvider implements IEnumeration {

    /** No authentication provider selected. */
    NONE(0, "none"),

    /** WarrantyBee's internal authentication. */
    INTERNAL(1, "internal"),

    /** Authentication via Meta/Facebook. */
    FACEBOOK(2, "facebook"),

    /** Authentication via Google OAuth. */
    GOOGLE(3, "google"),

    /** Authentication via LinkedIn OAuth. */
    LINKEDIN(4, "linkedin");

    /** Numeric code representing the provider. */
    private final int code;

    /** String identifier of the provider. */
    private final String name;

    /**
     * Creates an AuthProvider with its associated code and identifier.
     */
    AuthProvider(int code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * Returns the matching AuthProvider for the given name.
     * Falls back to {@link AuthProvider#NONE} if not found.
     */
    public static AuthProvider getValue(String value) {
        try {
            return IEnumeration.valueOf(AuthProvider.class, value);
        } catch (IllegalArgumentException e) {
            return AuthProvider.NONE;
        }
    }

    /**
     * Returns the matching AuthProvider for the given numeric code.
     * Falls back to {@link AuthProvider#NONE} if not found.
     */
    public static AuthProvider getValue(int value) {
        try {
            return IEnumeration.valueOf(AuthProvider.class, value);
        } catch (IllegalArgumentException e) {
            return AuthProvider.NONE;
        }
    }
}
