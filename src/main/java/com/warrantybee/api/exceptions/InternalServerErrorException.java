package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when an unexpected condition occurs within the server
 * that prevents it from fulfilling the request.
 */
public class InternalServerErrorException extends APIException {

    /**
     * Constructs a new InternalServerErrorException with the default message
     * from {@link Error#INTERNAL_SERVER_ERROR}.
     */
    public InternalServerErrorException() {
        super(Error.INTERNAL_SERVER_ERROR);
    }

    /**
     * Constructs a new InternalServerErrorException with a custom message.
     *
     * @param message a descriptive message explaining the error
     */
    public InternalServerErrorException(String message) {
        super(Error.INTERNAL_SERVER_ERROR, message);
    }

    /**
     * Constructs a new InternalServerErrorException with a custom message and cause.
     *
     * @param message a descriptive message explaining the error
     * @param cause   the underlying cause of this exception (e.g., SQLException, IOException)
     */
    public InternalServerErrorException(String message, Throwable cause) {
        super(Error.INTERNAL_SERVER_ERROR, message);
        initCause(cause);
    }
}
