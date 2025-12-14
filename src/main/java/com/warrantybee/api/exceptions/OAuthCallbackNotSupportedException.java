package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the specified oAuth callback is not supported.
 */
public class OAuthCallbackNotSupportedException extends APIException {

    /**
     * Constructs a new OAuthCallbackNotSupportedException with the default message from {@link Error#OAUTH_CALLBACK_NOT_SUPPORTED}.
     */
    public OAuthCallbackNotSupportedException() {
        super(Error.OAUTH_CALLBACK_NOT_SUPPORTED);
    }

    /**
     * Constructs a new OAuthCallbackNotSupportedException with a custom message.
     *
     * @param message custom description of the error
     */
    public OAuthCallbackNotSupportedException(String message) {
        super(Error.OAUTH_CALLBACK_NOT_SUPPORTED, message);
    }

    /**
     * Constructs a new OAuthCallbackNotSupportedException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public OAuthCallbackNotSupportedException(String message, Throwable cause) {
        super(Error.OAUTH_CALLBACK_NOT_SUPPORTED, message);
        initCause(cause);
    }
}
