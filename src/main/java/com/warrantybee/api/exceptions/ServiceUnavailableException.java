package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Exception thrown when an external service or dependency (e.g., Upstash, database, API)
 * is unavailable or cannot be reached.
 * <p>
 * Use this exception to indicate temporary service outages or connectivity issues.
 * </p>
 */
public class ServiceUnavailableException extends APIException {

    /**
     * Constructs a new ServiceUnavailableException with the default message
     * from {@link Error#INTERNAL_SERVER_ERROR}.
     */
    public ServiceUnavailableException() {
        super(Error.INTERNAL_SERVER_ERROR);
    }

    /**
     * Constructs a new ServiceUnavailableException with a custom message.
     *
     * @param message detailed description of the service unavailability
     */
    public ServiceUnavailableException(String message) {
        super(Error.INTERNAL_SERVER_ERROR, message);
    }

    /**
     * Constructs a new ServiceUnavailableException with a custom message and cause.
     *
     * @param message detailed description of the service unavailability
     * @param cause   the underlying exception that triggered this failure
     */
    public ServiceUnavailableException(String message, Throwable cause) {
        super(Error.INTERNAL_SERVER_ERROR, message);
        initCause(cause);
    }
}
