package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the culture is not specified.
 */
public class CultureRequiredException extends APIException {

    /**
     * Constructs a new CultureRequiredException with the default message from {@link Error#CULTURE_REQUIRED}.
     */
    public CultureRequiredException() {
        super(Error.CULTURE_REQUIRED);
    }

    /**
     * Constructs a new CultureRequiredException with a custom message.
     *
     * @param message custom description of the error
     */
    public CultureRequiredException(String message) {
        super(Error.CULTURE_REQUIRED, message);
    }

    /**
     * Constructs a new CultureRequiredException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public CultureRequiredException(String message, Throwable cause) {
        super(Error.CULTURE_REQUIRED, message);
        initCause(cause);
    }
}
