package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when an error occurs during user registration.
 */
public class UserRegistrationFailedException extends APIException {

    /**
     * Constructs a new UserRegistrationFailedException with the default message from {@link Error#USER_REGISTRATION_FAILED}.
     */
    public UserRegistrationFailedException() {
        super(Error.USER_REGISTRATION_FAILED);
    }

    /**
     * Constructs a new UserRegistrationFailedException with a custom message.
     *
     * @param message custom description of the user registration error
     */
    public UserRegistrationFailedException(String message) {
        super(Error.USER_REGISTRATION_FAILED, message);
    }

    /**
     * Constructs a new UserRegistrationFailedException with a custom message and cause.
     *
     * @param message custom description of the user registration error
     * @param cause   the underlying cause of the exception
     */
    public UserRegistrationFailedException(String message, Throwable cause) {
        super(Error.USER_REGISTRATION_FAILED, message);
        initCause(cause);
    }
}
