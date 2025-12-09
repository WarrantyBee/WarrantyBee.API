package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the specified authentication provider is not supported.
 */
public class AuthProviderNotSupportedException extends APIException {

    /**
     * Constructs a new AuthProviderNotSupportedException with the default message from {@link Error#AUTH_PROVIDER_NOT_SUPPORTED}.
     */
    public AuthProviderNotSupportedException() {
        super(Error.AUTH_PROVIDER_NOT_SUPPORTED);
    }

    /**
     * Constructs a new AuthProviderNotSupportedException with a custom message.
     *
     * @param message custom description of the error
     */
    public AuthProviderNotSupportedException(String message) {
        super(Error.AUTH_PROVIDER_NOT_SUPPORTED, message);
    }

    /**
     * Constructs a new AuthProviderNotSupportedException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public AuthProviderNotSupportedException(String message, Throwable cause) {
        super(Error.AUTH_PROVIDER_NOT_SUPPORTED, message);
        initCause(cause);
    }
}
