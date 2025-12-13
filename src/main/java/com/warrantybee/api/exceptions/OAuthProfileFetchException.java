package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the OAuth profile fetch failed.
 */
public class OAuthProfileFetchException extends APIException {

    /**
     * Constructs a new OAuthProfileFetchException with the default message from {@link Error#OAUTH_PROFILE_FETCH_FAILED}.
     */
    public OAuthProfileFetchException() {
        super(Error.OAUTH_PROFILE_FETCH_FAILED);
    }

    /**
     * Constructs a new OAuthProfileFetchException with a custom message.
     *
     * @param message custom description of the error
     */
    public OAuthProfileFetchException(String message) {
        super(Error.OAUTH_PROFILE_FETCH_FAILED, message);
    }

    /**
     * Constructs a new OAuthProfileFetchException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public OAuthProfileFetchException(String message, Throwable cause) {
        super(Error.OAUTH_PROFILE_FETCH_FAILED, message);
        initCause(cause);
    }
}
