package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when an error occurs during profile update.
 */
public class ProfileCouldNotBeUpdatedException extends APIException {

    /**
     * Constructs a new ProfileCouldNotBeUpdatedException with the default message from {@link Error#PROFILE_COULD_NOT_BE_UPDATED}.
     */
    public ProfileCouldNotBeUpdatedException() {
        super(Error.PROFILE_COULD_NOT_BE_UPDATED);
    }

    /**
     * Constructs a new ProfileCouldNotBeUpdatedException with a custom message.
     *
     * @param message custom description of the error
     */
    public ProfileCouldNotBeUpdatedException(String message) {
        super(Error.PROFILE_COULD_NOT_BE_UPDATED, message);
    }

    /**
     * Constructs a new ProfileCouldNotBeUpdatedException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public ProfileCouldNotBeUpdatedException(String message, Throwable cause) {
        super(Error.PROFILE_COULD_NOT_BE_UPDATED, message);
        initCause(cause);
    }
}
