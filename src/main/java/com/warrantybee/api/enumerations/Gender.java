package com.warrantybee.api.enumerations;

import com.warrantybee.api.enumerations.interfaces.IEnumeration;
import lombok.Getter;

/**
 * Enumeration representing the possible gender options for a user profile.
 * It implements {@link IEnumeration} to provide a unique integer code for each option.
 */
@Getter
public enum Gender implements IEnumeration {

    /** The gender of the user is not specified (Code: 0). */
    NONE(0),

    /** The user identifies as Male (Code: 1). */
    MALE(1),

    /** The user identifies as Female (Code: 2). */
    FEMALE(2),

    /** The user has opted not to disclose their gender (Code: 3). */
    PREFER_NOT_TO_SAY(3);

    /**
     * The unique, persistent integer code associated with this gender option.
     */
    private final int code;

    /**
     * Constructs a {@code Gender} enum constant with the specified code.
     *
     * @param code The unique integer code.
     */
    Gender(int code) {
        this.code = code;
    }
}
