package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when a JWT token is invalid, expired, or malformed.
 */
public class InvalidTokenException extends APIException {

  /**
   * Constructs a new InvalidTokenException with the default message
   * from {@link Error#INVALID_TOKEN}.
   */
  public InvalidTokenException() {
    super(Error.INVALID_TOKEN);
  }

  /**
   * Constructs a new InvalidTokenException with a custom message.
   *
   * @param message a detailed description of the token validation error
   */
  public InvalidTokenException(String message) {
    super(Error.INVALID_TOKEN, message);
  }

  /**
   * Constructs a new InvalidTokenException with a custom message and cause.
   *
   * @param message a detailed description of the token validation error
   * @param cause   the underlying cause of this exception (e.g., JWTVerificationException)
   */
  public InvalidTokenException(String message, Throwable cause) {
    super(Error.INVALID_TOKEN, message);
    initCause(cause);
  }
}
