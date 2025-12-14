package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the specified authorization code is invalid.
 */
public class InvalidAuthorizationCodeException extends APIException {

    /**
     * Constructs a new InvalidAuthorizationCodeException with the default message from {@link Error#INVALID_AUTHORIZATION_CODE}.
     */
    public InvalidAuthorizationCodeException() {
        super(Error.INVALID_AUTHORIZATION_CODE);
    }

    /**
     * Constructs a new InvalidAuthorizationCodeException with a custom message.
     *
     * @param message custom description of the error
     */
    public InvalidAuthorizationCodeException(String message) {
        super(Error.INVALID_AUTHORIZATION_CODE, message);
    }

    /**
     * Constructs a new InvalidAuthorizationCodeException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public InvalidAuthorizationCodeException(String message, Throwable cause) {
        super(Error.INVALID_AUTHORIZATION_CODE, message);
        initCause(cause);
    }
}
