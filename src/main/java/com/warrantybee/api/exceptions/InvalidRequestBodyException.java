package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Exception thrown when the request body is invalid or malformed.
 */
public class InvalidRequestBodyException extends APIException {

    /**
     * Creates a new exception with the default INVALID_REQUEST_BODY error.
     */
    public InvalidRequestBodyException() {
        super(Error.INVALID_REQUEST_BODY);
    }

    /**
     * Creates a new exception with a custom message.
     *
     * @param message the detail message
     */
    public InvalidRequestBodyException(String message) {
        super(Error.INVALID_REQUEST_BODY, message);
    }

    /**
     * Creates a new exception with a custom message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public InvalidRequestBodyException(String message, Throwable cause) {
        super(Error.INVALID_REQUEST_BODY, message);
        initCause(cause);
    }
}
