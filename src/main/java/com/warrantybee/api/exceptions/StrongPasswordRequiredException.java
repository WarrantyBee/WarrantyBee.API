package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the password is not strong enough.
 */
public class StrongPasswordRequiredException extends APIException {

    /**
     * Constructs a new StrongPasswordRequiredException with the default message from {@link Error#STRONG_PASSWORD_REQUIRED}.
     */
    public StrongPasswordRequiredException() {
        super(Error.STRONG_PASSWORD_REQUIRED);
    }

    /**
     * Constructs a new StrongPasswordRequiredException with a custom message.
     *
     * @param message custom description of the error
     */
    public StrongPasswordRequiredException(String message) {
        super(Error.STRONG_PASSWORD_REQUIRED, message);
    }

    /**
     * Constructs a new StrongPasswordRequiredException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public StrongPasswordRequiredException(String message, Throwable cause) {
        super(Error.STRONG_PASSWORD_REQUIRED, message);
        initCause(cause);
    }
}
