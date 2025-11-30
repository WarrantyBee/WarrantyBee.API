package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when an uploaded file has an invalid format.
 */
public class InvalidFileFormatException extends APIException {

    /**
     * Constructs a new InvalidFileFormatException with the default message from {@link Error#INVALID_FILE_FORMAT}.
     */
    public InvalidFileFormatException() {
        super(Error.INVALID_FILE_FORMAT);
    }

    /**
     * Constructs a new InvalidFileFormatException with a custom message.
     *
     * @param message custom description of the error
     */
    public InvalidFileFormatException(String message) {
        super(Error.INVALID_FILE_FORMAT, message);
    }

    /**
     * Constructs a new InvalidFileFormatException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public InvalidFileFormatException(String message, Throwable cause) {
        super(Error.INVALID_FILE_FORMAT, message);
        initCause(cause);
    }
}
