package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the lastname is required but not provided.
 */
public class LastnameRequiredException extends APIException {

    /**
     * Constructs a new LastnameRequiredException with the default message from {@link Error#LASTNAME_REQUIRED}.
     */
    public LastnameRequiredException() {
        super(Error.LASTNAME_REQUIRED);
    }

    /**
     * Constructs a new LastnameRequiredException with a custom message.
     *
     * @param message custom description of the error
     */
    public LastnameRequiredException(String message) {
        super(Error.LASTNAME_REQUIRED, message);
    }

    /**
     * Constructs a new LastnameRequiredException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public LastnameRequiredException(String message, Throwable cause) {
        super(Error.LASTNAME_REQUIRED, message);
        initCause(cause);
    }
}
