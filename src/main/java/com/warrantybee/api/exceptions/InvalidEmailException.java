package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the email format is invalid.
 */
public class InvalidEmailException extends APIException {

    /**
     * Constructs a new InvalidEmailException with the default message from {@link Error#INVALID_EMAIL}.
     */
    public InvalidEmailException() {
        super(Error.INVALID_EMAIL);
    }

    /**
     * Constructs a new InvalidEmailException with a custom message.
     *
     * @param message custom description of the error
     */
    public InvalidEmailException(String message) {
        super(Error.INVALID_EMAIL, message);
    }

    /**
     * Constructs a new InvalidEmailException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public InvalidEmailException(String message, Throwable cause) {
        super(Error.INVALID_EMAIL, message);
        initCause(cause);
    }
}
