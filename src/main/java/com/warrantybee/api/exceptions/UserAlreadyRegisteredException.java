package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

<<<<<<< HEAD
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
=======
/**
 * Thrown when a user is already registered in the system.
 */
public class UserAlreadyRegisteredException extends APIException {

    /**
     * Constructs a new UserAlreadyRegisteredException with the default message
     * from {@link Error#USER_ALREADY_REGISTERED}.
     */
    public UserAlreadyRegisteredException() {
        super(Error.USER_ALREADY_REGISTERED);
    }

    /**
     * Constructs a new UserAlreadyRegisteredException with a custom message.
>>>>>>> c9d7bb07b095b0d806ba915930111a3f05f419c9
     *
     * @param message a detailed description of the error
     */
    public UserAlreadyRegisteredException(String message) {
<<<<<<< HEAD
      super(Error.USER_ALREADY_REGISTERED, message);
    }

    /**
     * Constructs a new UserNotFoundException with a custom message and cause.
=======
        super(Error.USER_ALREADY_REGISTERED, message);
    }

    /**
     * Constructs a new UserAlreadyRegisteredException with a custom message and cause.
>>>>>>> c9d7bb07b095b0d806ba915930111a3f05f419c9
     *
     * @param message a detailed description of the error
     * @param cause   the underlying cause of this exception
     */
    public UserAlreadyRegisteredException(String message, Throwable cause) {
<<<<<<< HEAD
      super(Error.USER_ALREADY_REGISTERED, message);
      initCause(cause);
    }
}
=======
        super(Error.USER_ALREADY_REGISTERED, message);
        initCause(cause);
    }
}
>>>>>>> c9d7bb07b095b0d806ba915930111a3f05f419c9
