package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the access token exchange failed.
 */
public class AccessTokenExchangeException extends APIException {

    /**
     * Constructs a new AccessTokenExchangeException with the default message from {@link Error#ACCESS_TOKEN_EXCHANGE_FAILED}.
     */
    public AccessTokenExchangeException() {
        super(Error.ACCESS_TOKEN_EXCHANGE_FAILED);
    }

    /**
     * Constructs a new AccessTokenExchangeException with a custom message.
     *
     * @param message custom description of the error
     */
    public AccessTokenExchangeException(String message) {
        super(Error.ACCESS_TOKEN_EXCHANGE_FAILED, message);
    }

    /**
     * Constructs a new AccessTokenExchangeException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public AccessTokenExchangeException(String message, Throwable cause) {
        super(Error.ACCESS_TOKEN_EXCHANGE_FAILED, message);
        initCause(cause);
    }
}
