package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the OTP has expired.
 */
public class OtpExpiredException extends APIException {

    /**
     * Constructs a new OtpExpiredException with the default message from {@link Error#OTP_EXPIRED}.
     */
    public OtpExpiredException() {
        super(Error.OTP_EXPIRED);
    }

    /**
     * Constructs a new OtpExpiredException with a custom message.
     *
     * @param message custom description of the error
     */
    public OtpExpiredException(String message) {
        super(Error.OTP_EXPIRED, message);
    }

    /**
     * Constructs a new OtpExpiredException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public OtpExpiredException(String message, Throwable cause) {
        super(Error.OTP_EXPIRED, message);
        initCause(cause);
    }
}
