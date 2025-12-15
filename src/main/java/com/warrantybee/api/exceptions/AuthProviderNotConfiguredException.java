package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the specified authentication provider is not configured.
 */
public class AuthProviderNotConfiguredException extends APIException {

    /**
     * Constructs a new AuthProviderNotConfiguredException with the default message from {@link Error#AUTH_PROVIDER_NOT_CONFIGURED}.
     */
    public AuthProviderNotConfiguredException() {
        super(Error.AUTH_PROVIDER_NOT_CONFIGURED);
    }

    /**
     * Constructs a new AuthProviderNotConfiguredException with a custom message.
     *
     * @param message custom description of the error
     */
    public AuthProviderNotConfiguredException(String message) {
        super(Error.AUTH_PROVIDER_NOT_CONFIGURED, message);
    }

    /**
     * Constructs a new AuthProviderNotConfiguredException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public AuthProviderNotConfiguredException(String message, Throwable cause) {
        super(Error.AUTH_PROVIDER_NOT_CONFIGURED, message);
        initCause(cause);
    }
}
