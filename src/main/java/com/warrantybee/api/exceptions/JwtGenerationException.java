package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when an error occurs while generating a JWT token.
 */
public class JwtGenerationException extends APIException {

  /**
   * Constructs a new JwtGenerationException with the default message
   * from {@link Error#JWT_GENERATION_ERROR}.
   */
  public JwtGenerationException() {
    super(Error.JWT_GENERATION_ERROR);
  }

  /**
   * Constructs a new JwtGenerationException with a custom message.
   *
   * @param message a detailed description of the token generation error
   */
  public JwtGenerationException(String message) {
    super(Error.JWT_GENERATION_ERROR, message);
  }

  /**
   * Constructs a new JwtGenerationException with a custom message and cause.
   *
   * @param message a detailed description of the token generation error
   * @param cause   the underlying cause of this exception
   */
  public JwtGenerationException(String message, Throwable cause) {
    super(Error.JWT_GENERATION_ERROR, message);
    initCause(cause);
  }
}
