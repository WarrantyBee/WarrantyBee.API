package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the provided login credentials (e.g., email and password) are incorrect.
 */
public class InvalidCredentialsException extends APIException {

  /**
   * Constructs a new InvalidCredentialsException with the default message
   * from {@link Error#INVALID_LOGIN_CREDENTIALS}.
   */
  public InvalidCredentialsException() {
    super(Error.INVALID_LOGIN_CREDENTIALS);
  }

  /**
   * Constructs a new InvalidCredentialsException with a custom message.
   *
   * @param message a detailed description of the error
   */
  public InvalidCredentialsException(String message) {
    super(Error.INVALID_LOGIN_CREDENTIALS, message);
  }

  /**
   * Constructs a new InvalidCredentialsException with a custom message and cause.
   *
   * @param message a detailed description of the error
   * @param cause   the underlying cause of this exception
   */
  public InvalidCredentialsException(String message, Throwable cause) {
    super(Error.INVALID_LOGIN_CREDENTIALS, message);
    initCause(cause);
  }
}
