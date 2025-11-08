package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when a password reset operation fails.
 */
public class PasswordResetFailedException extends APIException {

    /**
     * Constructs a new PasswordResetFailedException with the default message from {@link Error#PASSWORD_RESET_FAILED}.
     */
    public PasswordResetFailedException() {
        super(Error.PASSWORD_RESET_FAILED);
    }

    /**
     * Constructs a new PasswordResetFailedException with a custom message.
     *
     * @param message custom description of the error
     */
    public PasswordResetFailedException(String message) {
        super(Error.PASSWORD_RESET_FAILED, message);
    }

    /**
     * Constructs a new PasswordResetFailedException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public PasswordResetFailedException(String message, Throwable cause) {
        super(Error.PASSWORD_RESET_FAILED, message);
        initCause(cause);
    }
}
