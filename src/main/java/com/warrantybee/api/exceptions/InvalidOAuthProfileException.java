package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the OAuth profile is invalid.
 */
public class InvalidOAuthProfileException extends APIException {

    /**
     * Constructs a new InvalidOAuthProfileException with the default message from {@link Error#INVALID_OAUTH_PROFILE}.
     */
    public InvalidOAuthProfileException() {
        super(Error.INVALID_OAUTH_PROFILE);
    }

    /**
     * Constructs a new InvalidOAuthProfileException with a custom message.
     *
     * @param message custom description of the error
     */
    public InvalidOAuthProfileException(String message) {
        super(Error.INVALID_OAUTH_PROFILE, message);
    }

    /**
     * Constructs a new InvalidOAuthProfileException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public InvalidOAuthProfileException(String message, Throwable cause) {
        super(Error.INVALID_OAUTH_PROFILE, message);
        initCause(cause);
    }
}
