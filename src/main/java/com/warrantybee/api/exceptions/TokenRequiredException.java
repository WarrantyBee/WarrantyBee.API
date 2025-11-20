package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Exception thrown when a required authentication token is missing.
 */
public class TokenRequiredException extends APIException {

    /**
     * Creates a new exception with the default TOKEN_REQUIRED error.
     */
    public TokenRequiredException() {
        super(Error.TOKEN_REQUIRED);
    }

    /**
     * Creates a new exception with a custom message.
     *
     * @param message the detail message
     */
    public TokenRequiredException(String message) {
        super(Error.TOKEN_REQUIRED, message);
    }

    /**
     * Creates a new exception with a custom message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public TokenRequiredException(String message, Throwable cause) {
        super(Error.TOKEN_REQUIRED, message);
        initCause(cause);
    }
}
