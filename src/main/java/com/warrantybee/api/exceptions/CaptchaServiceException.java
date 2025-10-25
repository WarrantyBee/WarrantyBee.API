package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when an error occurs while interacting with the captcha service.
 */
public class CaptchaServiceException extends APIException {

  /**
   * Constructs a new CaptchaServiceException with the default message
   * from {@link Error#CAPTCHA_SERVICE_ERROR}.
   */
  public CaptchaServiceException() {
    super(Error.CAPTCHA_SERVICE_ERROR);
  }

  /**
   * Constructs a new CaptchaServiceException with a custom message.
   *
   * @param message a detailed description of the captcha service error
   */
  public CaptchaServiceException(String message) {
    super(Error.CAPTCHA_SERVICE_ERROR, message);
  }

  /**
   * Constructs a new CaptchaServiceException with a custom message and cause.
   *
   * @param message a detailed description of the captcha service error
   * @param cause   the underlying cause of this exception
   */
  public CaptchaServiceException(String message, Throwable cause) {
    super(Error.CAPTCHA_SERVICE_ERROR, message);
    initCause(cause);
  }
}
