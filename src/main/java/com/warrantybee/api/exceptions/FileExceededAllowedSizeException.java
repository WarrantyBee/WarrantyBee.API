package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when an uploaded file has exceeded the allowed size.
 */
public class FileExceededAllowedSizeException extends APIException {

    /**
     * Constructs a new FileExceededAllowedSizeException with the default message from {@link Error#FILE_EXCEEDED_ALLOWED_SIZE}.
     */
    public FileExceededAllowedSizeException() {
        super(Error.FILE_EXCEEDED_ALLOWED_SIZE);
    }

    /**
     * Constructs a new FileExceededAllowedSizeException with a custom message.
     *
     * @param message custom description of the error
     */
    public FileExceededAllowedSizeException(String message) {
        super(Error.FILE_EXCEEDED_ALLOWED_SIZE, message);
    }

    /**
     * Constructs a new FileExceededAllowedSizeException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public FileExceededAllowedSizeException(String message, Throwable cause) {
        super(Error.FILE_EXCEEDED_ALLOWED_SIZE, message);
        initCause(cause);
    }
}
