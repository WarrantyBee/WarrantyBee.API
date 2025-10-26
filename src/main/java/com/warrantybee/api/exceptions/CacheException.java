package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when an error occurs during cache operations (e.g., connection failure, parsing error, etc.).
 */
public class CacheException extends APIException {

    /**
     * Constructs a new CacheException with the default message from {@link Error#CACHE_ERROR}.
     */
    public CacheException() {
        super(Error.CACHE_ERROR);
    }

    /**
     * Constructs a new CacheException with a custom message.
     *
     * @param message custom description of the cache error
     */
    public CacheException(String message) {
        super(Error.CACHE_ERROR, message);
    }

    /**
     * Constructs a new CacheException with a custom message and cause.
     *
     * @param message custom description of the cache error
     * @param cause   the underlying cause of the exception
     */
    public CacheException(String message, Throwable cause) {
        super(Error.CACHE_ERROR, message);
        initCause(cause);
    }
}
