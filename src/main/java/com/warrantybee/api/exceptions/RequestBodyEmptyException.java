package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the request body is empty.
 */
public class RequestBodyEmptyException extends APIException {

    /**
     * Constructs a new RequestBodyEmptyException with the default message
     * from {@link Error#REQUEST_BODY_EMPTY}.
     */
    public RequestBodyEmptyException() {
        super(Error.REQUEST_BODY_EMPTY);
    }

    /**
     * Constructs a new RequestBodyEmptyException with a custom message.
     *
     * @param message a detailed description of the error
     */
    public RequestBodyEmptyException(String message) {
        super(Error.REQUEST_BODY_EMPTY, message);
    }

    /**
     * Constructs a new RequestBodyEmptyException with a custom message and cause.
     *
     * @param message a detailed description of the error
     * @param cause   the underlying cause of this exception
     */
    public RequestBodyEmptyException(String message, Throwable cause) {
        super(Error.REQUEST_BODY_EMPTY, message);
        initCause(cause);
    }
}
