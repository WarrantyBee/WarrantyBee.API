package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the firstname is required but not provided.
 */
public class FirstnameRequiredException extends APIException {

    /**
     * Constructs a new FirstnameRequiredException with the default message from {@link Error#FIRSTNAME_REQUIRED}.
     */
    public FirstnameRequiredException() {
        super(Error.FIRSTNAME_REQUIRED);
    }

    /**
     * Constructs a new FirstnameRequiredException with a custom message.
     *
     * @param message custom description of the error
     */
    public FirstnameRequiredException(String message) {
        super(Error.FIRSTNAME_REQUIRED, message);
    }

    /**
     * Constructs a new FirstnameRequiredException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public FirstnameRequiredException(String message, Throwable cause) {
        super(Error.FIRSTNAME_REQUIRED, message);
        initCause(cause);
    }
}