package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when an authorization code is required but not provided.
 */
public class AuthorizationCodeRequired extends APIException {

    /**
     * Constructs a new AuthorizationCodeRequired with the default message from {@link Error#AUTHORIZATION_CODE_REQUIRED}.
     */
    public AuthorizationCodeRequired() {
        super(Error.AUTHORIZATION_CODE_REQUIRED);
    }

    /**
     * Constructs a new AuthorizationCodeRequired with a custom message.
     *
     * @param message custom description of the error
     */
    public AuthorizationCodeRequired(String message) {
        super(Error.AUTHORIZATION_CODE_REQUIRED, message);
    }

    /**
     * Constructs a new AuthorizationCodeRequired with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public AuthorizationCodeRequired(String message, Throwable cause) {
        super(Error.AUTHORIZATION_CODE_REQUIRED, message);
        initCause(cause);
    }
}
