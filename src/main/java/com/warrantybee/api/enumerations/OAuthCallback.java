package com.warrantybee.api.enumerations;

import com.warrantybee.api.enumerations.interfaces.IEnumeration;
import lombok.Getter;

/**
 * Represents OAuth callback actions used to determine
 * the authentication flow during OAuth redirection.
 */
@Getter
public enum OAuthCallback implements IEnumeration {

    /** No OAuth action specified */
    NONE(0, "none"),

    /** OAuth flow for user sign-up */
    SIGN_UP(1, "signup"),

    /** OAuth flow for user sign-in */
    SIGN_IN(2, "signin");

    /** Unique numeric code for the callback action */
    private final int code;

    /** String identifier used in OAuth callbacks */
    private final String name;

    OAuthCallback(int code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * Returns the {@link OAuthCallback} corresponding to the given numeric value.
     * If no matching value is found, {@link OAuthCallback#NONE} is returned.
     *
     * @param value the numeric callback code
     * @return the matching {@link OAuthCallback} or {@link OAuthCallback#NONE} if invalid
     */
    public static OAuthCallback getValue(int value) {
        try {
            return IEnumeration.valueOf(OAuthCallback.class, value);
        }
        catch (IllegalArgumentException e) {
            return OAuthCallback.NONE;
        }
    }

    /**
     * Returns the {@link OAuthCallback} corresponding to the given string value.
     * If no matching value is found, {@link OAuthCallback#NONE} is returned.
     *
     * @param value the callback name
     * @return the matching {@link OAuthCallback} or {@link OAuthCallback#NONE} if invalid
     */
    public static OAuthCallback getValue(String value) {
        try {
            return IEnumeration.valueOf(OAuthCallback.class, value);
        }
        catch (IllegalArgumentException e) {
            return OAuthCallback.NONE;
        }
    }
}
