package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when a user attempts to access a resource they do not have permission for.
 */
public class UnauthorizedAccessException extends APIException {

  /**
   * Constructs a new UnauthorizedAccessException with the default message
   * from {@link Error#UNAUTHORIZED_ACCESS}.
   */
  public UnauthorizedAccessException() {
    super(Error.UNAUTHORIZED_ACCESS);
  }

  /**
   * Constructs a new UnauthorizedAccessException with a custom message.
   *
   * @param message custom description of the unauthorized access
   */
  public UnauthorizedAccessException(String message) {
    super(Error.UNAUTHORIZED_ACCESS, message);
  }

  /**
   * Constructs a new UnauthorizedAccessException with a custom message and cause.
   *
   * @param message custom description of the unauthorized access
   * @param cause   the underlying cause of the exception
   */
  public UnauthorizedAccessException(String message, Throwable cause) {
    super(Error.UNAUTHORIZED_ACCESS, message);
    initCause(cause);
  }
}
