package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when an uploaded file is empty.
 */
public class FileIsEmptyException extends APIException {

    /**
     * Constructs a new FileIsEmptyException with the default message from {@link Error#FILE_IS_EMPTY}.
     */
    public FileIsEmptyException() {
        super(Error.FILE_IS_EMPTY);
    }

    /**
     * Constructs a new FileIsEmptyException with a custom message.
     *
     * @param message custom description of the error
     */
    public FileIsEmptyException(String message) {
        super(Error.FILE_IS_EMPTY, message);
    }

    /**
     * Constructs a new FileIsEmptyException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public FileIsEmptyException(String message, Throwable cause) {
        super(Error.FILE_IS_EMPTY, message);
        initCause(cause);
    }
}
