package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when an error occurs during storage service operations.
 */
public class StorageServiceException extends APIException {

    /**
     * Constructs a new StorageServiceException with the default message from {@link Error#STORAGE_SERVICE_ERROR}.
     */
    public StorageServiceException() {
        super(Error.STORAGE_SERVICE_ERROR);
    }

    /**
     * Constructs a new StorageServiceException with a custom message.
     *
     * @param message custom description of the error
     */
    public StorageServiceException(String message) {
        super(Error.STORAGE_SERVICE_ERROR, message);
    }

    /**
     * Constructs a new StorageServiceException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public StorageServiceException(String message, Throwable cause) {
        super(Error.STORAGE_SERVICE_ERROR, message);
        initCause(cause);
    }
}
