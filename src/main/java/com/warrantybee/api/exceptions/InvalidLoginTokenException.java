package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the login token is invalid.
 */
public class InvalidLoginTokenException extends APIException {

    /**
     * Constructs a new InvalidLoginTokenException with the default message from {@link Error#INVALID_TOKEN}.
     */
    public InvalidLoginTokenException() {
        super(Error.INVALID_TOKEN);
    }

    /**
     * Constructs a new InvalidLoginTokenException with a custom message.
     *
     * @param message custom description of the error
     */
    public InvalidLoginTokenException(String message) {
        super(Error.INVALID_TOKEN, message);
    }

    /**
     * Constructs a new InvalidLoginTokenException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public InvalidLoginTokenException(String message, Throwable cause) {
        super(Error.INVALID_TOKEN, message);
        initCause(cause);
    }
}
