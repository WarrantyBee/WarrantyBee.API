package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the JWT token is expired.
 */
public class SessionExpiredException extends APIException {

    /**
     * Constructs a new SessionExpiredException with the default message from {@link Error#SESSION_EXPIRED}.
     */
    public SessionExpiredException() {
        super(Error.SESSION_EXPIRED);
    }

    /**
     * Constructs a new SessionExpiredException with a custom message.
     *
     * @param message custom description of the error
     */
    public SessionExpiredException(String message) {
        super(Error.SESSION_EXPIRED, message);
    }

    /**
     * Constructs a new SessionExpiredException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public SessionExpiredException(String message, Throwable cause) {
        super(Error.SESSION_EXPIRED, message);
        initCause(cause);
    }
}
