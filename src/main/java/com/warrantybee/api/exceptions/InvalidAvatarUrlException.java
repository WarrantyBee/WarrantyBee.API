package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the provided avatar URL is invalid.
 */
public class InvalidAvatarUrlException extends APIException {

    /**
     * Constructs a new InvalidAvatarUrlException with the default message from {@link Error#INVALID_AVATAR_URL}.
     */
    public InvalidAvatarUrlException() {
        super(Error.INVALID_AVATAR_URL);
    }

    /**
     * Constructs a new InvalidAvatarUrlException with a custom message.
     *
     * @param message custom description of the error
     */
    public InvalidAvatarUrlException(String message) {
        super(Error.INVALID_AVATAR_URL, message);
    }

    /**
     * Constructs a new InvalidAvatarUrlException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public InvalidAvatarUrlException(String message, Throwable cause) {
        super(Error.INVALID_AVATAR_URL, message);
        initCause(cause);
    }
}
