package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

public class UserAlreadyRegisteredException extends APIException {
    /**
     * Constructs a new UserNotFoundException with the default message
     * from {@link com.warrantybee.api.enumerations.Error#USER_NOT_FOUND}.
     */
    public UserAlreadyRegisteredException() {
      super(Error.USER_ALREADY_REGISTERED);
    }

    /**
     * Constructs a new UserNotFoundException with a custom message.
     *
     * @param message a detailed description of the error
     */
    public UserAlreadyRegisteredException(String message) {
      super(Error.USER_ALREADY_REGISTERED, message);
    }

    /**
     * Constructs a new UserNotFoundException with a custom message and cause.
     *
     * @param message a detailed description of the error
     * @param cause   the underlying cause of this exception
     */
    public UserAlreadyRegisteredException(String message, Throwable cause) {
      super(Error.USER_ALREADY_REGISTERED, message);
      initCause(cause);
    }
}
