package com.warrantybee.api.enumerations;

import lombok.Getter;

/**
 * Enumeration representing user gender options.
 */
@Getter
public enum Gender {

    /** Gender not specified */
    NONE(0),

    /** Male gender */
    MALE(1),

    /** Female gender */
    FEMALE(2),

    /** Prefer not to disclose gender */
    PREFER_NOT_TO_SAY(3);

    /** Integer code for the gender */
    private final int code;

    /** Constructor */
    Gender(int code) {
        this.code = code;
    }
}
