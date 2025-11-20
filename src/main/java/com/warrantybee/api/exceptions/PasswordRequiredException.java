package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the password is required but not provided.
 */
public class PasswordRequiredException extends APIException {

    /**
     * Constructs a new PasswordRequiredException with the default message from {@link Error#PASSWORD_REQUIRED}.
     */
    public PasswordRequiredException() {
        super(Error.PASSWORD_REQUIRED);
    }

    /**
     * Constructs a new PasswordRequiredException with a custom message.
     *
     * @param message custom description of the error
     */
    public PasswordRequiredException(String message) {
        super(Error.PASSWORD_REQUIRED, message);
    }

    /**
     * Constructs a new PasswordRequiredException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public PasswordRequiredException(String message, Throwable cause) {
        super(Error.PASSWORD_REQUIRED, message);
        initCause(cause);
    }
}
