package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the specified access token is invalid.
 */
public class InvalidAccessTokenException extends APIException {

    /**
     * Constructs a new InvalidAccessTokenException with the default message from {@link Error#INVALID_ACCESS_TOKEN}.
     */
    public InvalidAccessTokenException() {
        super(Error.INVALID_ACCESS_TOKEN);
    }

    /**
     * Constructs a new InvalidAccessTokenException with a custom message.
     *
     * @param message custom description of the error
     */
    public InvalidAccessTokenException(String message) {
        super(Error.INVALID_ACCESS_TOKEN, message);
    }

    /**
     * Constructs a new InvalidAccessTokenException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public InvalidAccessTokenException(String message, Throwable cause) {
        super(Error.INVALID_ACCESS_TOKEN, message);
        initCause(cause);
    }
}
