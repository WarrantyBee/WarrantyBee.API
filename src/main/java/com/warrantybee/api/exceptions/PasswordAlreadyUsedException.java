package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

public class PasswordAlreadyUsedException extends APIException {
    /**
     * Constructs a new PasswordAlreadyUsedException with the default message from {@link com.warrantybee.api.enumerations.Error#PASSWORD_ALREADY_USED}.
     */
    public PasswordAlreadyUsedException() {
        super(Error.PASSWORD_ALREADY_USED);
    }

    /**
     * Constructs a new PasswordAlreadyUsedException with a custom message.
     *
     * @param message custom description of the error
     */
    public PasswordAlreadyUsedException(String message) {
        super(Error.PASSWORD_ALREADY_USED, message);
    }

    /**
     * Constructs a new PasswordAlreadyUsedException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public PasswordAlreadyUsedException(String message, Throwable cause) {
        super(Error.PASSWORD_ALREADY_USED, message);
        initCause(cause);
    }
}
