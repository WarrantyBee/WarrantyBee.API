package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when a client provides invalid or malformed input data.
 */
public class InvalidInputException extends APIException {

  /**
   * Constructs a new InvalidInputException with the default message from {@link Error#INVALID_INPUT}.
   */
  public InvalidInputException() {
    super(Error.INVALID_INPUT);
  }

  /**
   * Constructs a new InvalidInputException with a custom message.
   *
   * @param message custom description of the invalid input
   */
  public InvalidInputException(String message) {
    super(Error.INVALID_INPUT, message);
  }

  /**
   * Constructs a new InvalidInputException with a custom message and cause.
   *
   * @param message custom description of the invalid input
   * @param cause   the underlying cause of the exception
   */
  public InvalidInputException(String message, Throwable cause) {
    super(Error.INVALID_INPUT, message);
    initCause(cause);
  }
}
