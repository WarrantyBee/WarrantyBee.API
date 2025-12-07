package com.warrantybee.api.enumerations;

import com.warrantybee.api.enumerations.interfaces.IEnumeration;
import lombok.Getter;

/**
 * Defines the set of security permissions supported in the WarrantyBee platform.
 */
@Getter
public enum SecurityPermission implements IEnumeration {

    /**
     * Represents no permission or an undefined permission.
     */
    NONE(0, "NONE"),

    /**
     * Allows a user to edit their personal profile information.
     */
    EDIT_PROFILE(1, "EDIT_PROFILE"),

    /**
     * Allows a user to change the profile avatar.
     */
    CHANGE_AVATAR(2, "CHANGE_AVATAR"),

    /**
     * Allows users to access their profile.
     */
    ACCESS_PROFILE(3, "ACCESS_PROFILE");

    /**
     * The unique, persistent integer code associated with this permission.
     */
    private final int code;

    /**
     * The unique, persistent name of the permission.
     * */
    private final String name;

    /**
     * Constructs a new {@code SecurityPermission} value.
     */
    SecurityPermission(int code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * Returns the {@link SecurityPermission} matching the given name (case-insensitive).
     *
     * @param value the permission name
     * @return the matching {@code SecurityPermission}, or {@code NONE} if invalid
     */
    public static SecurityPermission getValue(String value) {
        try {
            return IEnumeration.valueOf(SecurityPermission.class, value);
        }
        catch (IllegalArgumentException e) {
            return SecurityPermission.NONE;
        }
    }

    /**
     * Returns the {@link SecurityPermission} matching the given integer code.
     *
     * @param value the permission code
     * @return the matching {@code SecurityPermission}, or {@code NONE} if invalid
     */
    public static SecurityPermission getValue(int value) {
        try {
            return IEnumeration.valueOf(SecurityPermission.class, value);
        }
        catch (IllegalArgumentException e) {
            return SecurityPermission.NONE;
        }
    }
}
