package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the user identifier from the auth provider is required.
 */
public class AuthProviderUserIdentifierRequiredException extends APIException {

    /**
     * Constructs a new AuthProviderUserIdentifierRequiredException with the default message from {@link Error#AUTH_PROVIDER_USER_IDENTIFIER_REQUIRED}.
     */
    public AuthProviderUserIdentifierRequiredException() {
        super(Error.AUTH_PROVIDER_USER_IDENTIFIER_REQUIRED);
    }

    /**
     * Constructs a new AuthProviderUserIdentifierRequiredException with a custom message.
     *
     * @param message custom description of the error
     */
    public AuthProviderUserIdentifierRequiredException(String message) {
        super(Error.AUTH_PROVIDER_USER_IDENTIFIER_REQUIRED, message);
    }

    /**
     * Constructs a new AuthProviderUserIdentifierRequiredException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public AuthProviderUserIdentifierRequiredException(String message, Throwable cause) {
        super(Error.AUTH_PROVIDER_USER_IDENTIFIER_REQUIRED, message);
        initCause(cause);
    }
}
