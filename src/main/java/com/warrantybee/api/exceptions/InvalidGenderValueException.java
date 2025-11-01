package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the gender value is invalid.
 */
public class InvalidGenderValueException extends APIException {

    /**
     * Constructs a new InvalidGenderValueException with the default message from {@link Error#INVALID_GENDER_VALUE}.
     */
    public InvalidGenderValueException() {
        super(Error.INVALID_GENDER_VALUE);
    }

    /**
     * Constructs a new InvalidGenderValueException with a custom message.
     *
     * @param message custom description of the error
     */
    public InvalidGenderValueException(String message) {
        super(Error.INVALID_GENDER_VALUE, message);
    }

    /**
     * Constructs a new InvalidGenderValueException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public InvalidGenderValueException(String message, Throwable cause) {
        super(Error.INVALID_GENDER_VALUE, message);
        initCause(cause);
    }
}
