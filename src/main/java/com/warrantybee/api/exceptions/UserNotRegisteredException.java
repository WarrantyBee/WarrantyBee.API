package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when a user is not registered in the system.
 */
public class UserNotRegisteredException extends APIException {

  /**
   * Constructs a new UserNotRegisteredException with the default message
   * from {@link Error#USER_NOT_REGISTERED}.
   */
  public UserNotRegisteredException() {
    super(Error.USER_NOT_REGISTERED);
  }

  /**
   * Constructs a new UserNotRegisteredException with a custom message.
   *
   * @param message a detailed description of the error
   */
  public UserNotRegisteredException(String message) {
    super(Error.USER_NOT_REGISTERED, message);
  }

  /**
   * Constructs a new UserNotRegisteredException with a custom message and cause.
   *
   * @param message a detailed description of the error
   * @param cause   the underlying cause of this exception
   */
  public UserNotRegisteredException(String message, Throwable cause) {
    super(Error.USER_NOT_REGISTERED, message);
    initCause(cause);
  }
}
