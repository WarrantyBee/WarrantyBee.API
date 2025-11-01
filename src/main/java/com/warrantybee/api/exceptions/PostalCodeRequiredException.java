package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the postal code is required but not provided.
 */
public class PostalCodeRequiredException extends APIException {

    /**
     * Constructs a new PostalCodeRequiredException with the default message from {@link Error#POSTAL_CODE_REQUIRED}.
     */
    public PostalCodeRequiredException() {
        super(Error.POSTAL_CODE_REQUIRED);
    }

    /**
     * Constructs a new PostalCodeRequiredException with a custom message.
     *
     * @param message custom description of the error
     */
    public PostalCodeRequiredException(String message) {
        super(Error.POSTAL_CODE_REQUIRED, message);
    }

    /**
     * Constructs a new PostalCodeRequiredException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public PostalCodeRequiredException(String message, Throwable cause) {
        super(Error.POSTAL_CODE_REQUIRED, message);
        initCause(cause);
    }
}
