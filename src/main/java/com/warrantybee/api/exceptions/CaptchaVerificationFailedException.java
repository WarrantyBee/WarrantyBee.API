package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the provided captcha is invalid or not verified.
 */
public class CaptchaVerificationFailedException extends APIException {

  /**
   * Constructs a new InvalidCaptchaException with the default message
   * from {@link Error#INVALID_CAPTCHA}.
   */
  public CaptchaVerificationFailedException() {
    super(Error.INVALID_CAPTCHA);
  }

  /**
   * Constructs a new InvalidCaptchaException with a custom message.
   *
   * @param message a detailed description of the error
   */
  public CaptchaVerificationFailedException(String message) {
    super(Error.INVALID_CAPTCHA, message);
  }

  /**
   * Constructs a new InvalidCaptchaException with a custom message and cause.
   *
   * @param message a detailed description of the error
   * @param cause   the underlying cause of this exception
   */
  public CaptchaVerificationFailedException(String message, Throwable cause) {
    super(Error.INVALID_CAPTCHA, message);
    initCause(cause);
  }
}
