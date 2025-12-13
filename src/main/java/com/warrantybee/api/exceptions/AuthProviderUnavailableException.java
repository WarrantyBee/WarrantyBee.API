package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the specified auth provider is unavailable.
 */
public class AuthProviderUnavailableException extends APIException {

    /**
     * Constructs a new AuthProviderUnavailableException with the default message from {@link Error#AUTH_PROVIDER_UNAVAILABLE}.
     */
    public AuthProviderUnavailableException() {
        super(Error.AUTH_PROVIDER_UNAVAILABLE);
    }

    /**
     * Constructs a new AuthProviderUnavailableException with a custom message.
     *
     * @param message custom description of the error
     */
    public AuthProviderUnavailableException(String message) {
        super(Error.AUTH_PROVIDER_UNAVAILABLE, message);
    }

    /**
     * Constructs a new AuthProviderUnavailableException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public AuthProviderUnavailableException(String message, Throwable cause) {
        super(Error.AUTH_PROVIDER_UNAVAILABLE, message);
        initCause(cause);
    }
}
