package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the provided captcha is invalid.
 */
public class InvalidCaptchaException extends APIException {

  /**
   * Constructs a new InvalidCaptchaException with the default message
   * from {@link Error#INVALID_CAPTCHA}.
   */
  public InvalidCaptchaException() {
    super(Error.INVALID_CAPTCHA);
  }

  /**
   * Constructs a new InvalidCaptchaException with a custom message.
   *
   * @param message a detailed description of the error
   */
  public InvalidCaptchaException(String message) {
    super(Error.INVALID_CAPTCHA, message);
  }

  /**
   * Constructs a new InvalidCaptchaException with a custom message and cause.
   *
   * @param message a detailed description of the error
   * @param cause   the underlying cause of this exception
   */
  public InvalidCaptchaException(String message, Throwable cause) {
    super(Error.INVALID_CAPTCHA, message);
    initCause(cause);
  }
}
