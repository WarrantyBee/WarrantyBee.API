package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when a user is not found in the system.
 */
public class UserNotFoundException extends APIException {

    /**
     * Constructs a new UserNotFoundException with the default message
     * from {@link Error#USER_NOT_FOUND}.
     */
    public UserNotFoundException() {
      super(Error.USER_NOT_FOUND);
    }

    /**
     * Constructs a new UserNotFoundException with a custom message.
     *
     * @param message a detailed description of the error
     */
    public UserNotFoundException(String message) {
      super(Error.USER_NOT_FOUND, message);
    }

    /**
     * Constructs a new UserNotFoundException with a custom message and cause.
     *
     * @param message a detailed description of the error
     * @param cause   the underlying cause of this exception
     */
    public UserNotFoundException(String message, Throwable cause) {
      super(Error.USER_NOT_FOUND, message);
      initCause(cause);
    }
}
