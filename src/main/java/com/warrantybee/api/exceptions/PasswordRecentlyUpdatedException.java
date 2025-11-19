package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Exception thrown when a user attempts to reset their password
 * within a restricted cooldown period after a recent password update.
 */
public class PasswordRecentlyUpdatedException extends APIException {

    /**
     * Creates a new {@code PasswordRecentlyUpdatedException}
     * with the default error code {@link Error#PASSWORD_RECENTLY_UPDATED}.
     */
    public PasswordRecentlyUpdatedException() {
        super(Error.PASSWORD_RECENTLY_UPDATED);
    }

    /**
     * Creates a new {@code PasswordRecentlyUpdatedException}
     * with a custom error message.
     *
     * @param message a descriptive message explaining the error
     */
    public PasswordRecentlyUpdatedException(String message) {
        super(Error.PASSWORD_RECENTLY_UPDATED, message);
    }

    /**
     * Creates a new {@code PasswordRecentlyUpdatedException}
     * with a custom error message and an underlying cause.
     *
     * @param message a descriptive message explaining the error
     * @param cause   the underlying cause of this exception
     */
    public PasswordRecentlyUpdatedException(String message, Throwable cause) {
        super(Error.PASSWORD_RECENTLY_UPDATED, message);
        initCause(cause);
    }
}
