package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when an error occurs during avatar update.
 */
public class AvatarCouldNotBeUpdatedException extends APIException {

    /**
     * Constructs a new AvatarCouldNotBeUpdatedException with the default message from {@link Error#AVATAR_COULD_NOT_BE_UPDATED}.
     */
    public AvatarCouldNotBeUpdatedException() {
        super(Error.AVATAR_COULD_NOT_BE_UPDATED);
    }

    /**
     * Constructs a new AvatarCouldNotBeUpdatedException with a custom message.
     *
     * @param message custom description of the error
     */
    public AvatarCouldNotBeUpdatedException(String message) {
        super(Error.AVATAR_COULD_NOT_BE_UPDATED, message);
    }

    /**
     * Constructs a new AvatarCouldNotBeUpdatedException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public AvatarCouldNotBeUpdatedException(String message, Throwable cause) {
        super(Error.AVATAR_COULD_NOT_BE_UPDATED, message);
        initCause(cause);
    }
}
