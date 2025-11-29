package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the user identifier is required.
 */
public class UserIdentifierRequiredException extends APIException {

    /**
     * Constructs a new UserIdentifierRequiredException with the default message from {@link Error#USER_IDENTIFIER_REQUIRED}.
     */
    public UserIdentifierRequiredException() {
        super(Error.USER_IDENTIFIER_REQUIRED);
    }

    /**
     * Constructs a new UserIdentifierRequiredException with a custom message.
     *
     * @param message custom description of the error
     */
    public UserIdentifierRequiredException(String message) {
        super(Error.USER_IDENTIFIER_REQUIRED, message);
    }

    /**
     * Constructs a new UserIdentifierRequiredException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public UserIdentifierRequiredException(String message, Throwable cause) {
        super(Error.USER_IDENTIFIER_REQUIRED, message);
        initCause(cause);
    }
}
