package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the user is a minor.
 */
public class UserIsMinorException extends APIException {

    /**
     * Constructs a new UserIsMinorException with the default message from {@link Error#USER_IS_MINOR}.
     */
    public UserIsMinorException() {
        super(Error.USER_IS_MINOR);
    }

    /**
     * Constructs a new UserIsMinorException with a custom message.
     *
     * @param message custom description of the error
     */
    public UserIsMinorException(String message) {
        super(Error.USER_IS_MINOR, message);
    }

    /**
     * Constructs a new UserIsMinorException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public UserIsMinorException(String message, Throwable cause) {
        super(Error.USER_IS_MINOR, message);
        initCause(cause);
    }
}
