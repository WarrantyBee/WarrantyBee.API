package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the login token could not be saved.
 */
public class LoginTokenCouldNotBeSavedException extends APIException {

    /**
     * Constructs a new LoginTokenCouldNotBeSavedException with the default message from {@link Error#LOGIN_TOKEN_COULD_NOT_BE_SAVED}.
     */
    public LoginTokenCouldNotBeSavedException() {
        super(Error.LOGIN_TOKEN_COULD_NOT_BE_SAVED);
    }

    /**
     * Constructs a new LoginTokenCouldNotBeSavedException with a custom message.
     *
     * @param message custom description of the error
     */
    public LoginTokenCouldNotBeSavedException(String message) {
        super(Error.LOGIN_TOKEN_COULD_NOT_BE_SAVED, message);
    }

    /**
     * Constructs a new LoginTokenCouldNotBeSavedException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public LoginTokenCouldNotBeSavedException(String message, Throwable cause) {
        super(Error.LOGIN_TOKEN_COULD_NOT_BE_SAVED, message);
        initCause(cause);
    }
}