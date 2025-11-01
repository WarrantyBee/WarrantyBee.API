package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the captcha response is required.
 */
public class CaptchaResponseRequiredException extends APIException {

    /**
     * Constructs a new CaptchaResponseRequiredException with the default message
     * from {@link Error#CAPTCHA_RESPONSE_REQUIRED}.
     */
    public CaptchaResponseRequiredException() {
        super(Error.CAPTCHA_RESPONSE_REQUIRED);
    }

    /**
     * Constructs a new CaptchaResponseRequiredException with a custom message.
     *
     * @param message a detailed description of the error
     */
    public CaptchaResponseRequiredException(String message) {
        super(Error.CAPTCHA_RESPONSE_REQUIRED, message);
    }

    /**
     * Constructs a new CaptchaResponseRequiredException with a custom message and cause.
     *
     * @param message a detailed description of the error
     * @param cause   the underlying cause of this exception
     */
    public CaptchaResponseRequiredException(String message, Throwable cause) {
        super(Error.CAPTCHA_RESPONSE_REQUIRED, message);
        initCause(cause);
    }
}