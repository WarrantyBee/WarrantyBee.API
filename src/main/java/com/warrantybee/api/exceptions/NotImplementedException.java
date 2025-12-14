package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when a method is not implemented.
 */
public class NotImplementedException extends APIException {

    /**
     * Constructs a new NotImplementedException with the default message from {@link Error#NOT_IMPLEMENTED}.
     */
    public NotImplementedException() {
        super(Error.NOT_IMPLEMENTED);
    }

    /**
     * Constructs a new NotImplementedException with a custom message.
     *
     * @param message custom description of the error
     */
    public NotImplementedException(String message) {
        super(Error.NOT_IMPLEMENTED, message);
    }

    /**
     * Constructs a new NotImplementedException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public NotImplementedException(String message, Throwable cause) {
        super(Error.NOT_IMPLEMENTED, message);
        initCause(cause);
    }
}
