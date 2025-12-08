package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the specified login provider is not supported.
 */
public class LoginProviderNotSupportedException extends APIException {

    /**
     * Constructs a new LoginProviderNotSupportedException with the default message from {@link Error#LOGIN_PROVIDER_NOT_SUPPORTED}.
     */
    public LoginProviderNotSupportedException() {
        super(Error.LOGIN_PROVIDER_NOT_SUPPORTED);
    }

    /**
     * Constructs a new LoginProviderNotSupportedException with a custom message.
     *
     * @param message custom description of the error
     */
    public LoginProviderNotSupportedException(String message) {
        super(Error.LOGIN_PROVIDER_NOT_SUPPORTED, message);
    }

    /**
     * Constructs a new LoginProviderNotSupportedException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public LoginProviderNotSupportedException(String message, Throwable cause) {
        super(Error.LOGIN_PROVIDER_NOT_SUPPORTED, message);
        initCause(cause);
    }
}
