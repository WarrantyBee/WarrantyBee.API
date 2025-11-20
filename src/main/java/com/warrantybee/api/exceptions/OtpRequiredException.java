package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the OTP is required.
 */
public class OtpRequiredException extends APIException {

    /**
     * Constructs a new OtpRequiredException with the default message from {@link Error#OTP_REQUIRED}.
     */
    public OtpRequiredException() {
        super(Error.OTP_REQUIRED);
    }

    /**
     * Constructs a new OtpRequiredException with a custom message.
     *
     * @param message custom description of the error
     */
    public OtpRequiredException(String message) {
        super(Error.OTP_REQUIRED, message);
    }

    /**
     * Constructs a new OtpRequiredException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public OtpRequiredException(String message, Throwable cause) {
        super(Error.OTP_REQUIRED, message);
        initCause(cause);
    }
}
