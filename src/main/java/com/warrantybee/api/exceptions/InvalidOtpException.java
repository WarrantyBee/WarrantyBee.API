package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the OTP is invalid.
 */
public class InvalidOtpException extends APIException {

    /**
     * Constructs a new InvalidOtpException with the default message from {@link Error#INVALID_OTP}.
     */
    public InvalidOtpException() {
        super(Error.INVALID_OTP);
    }

    /**
     * Constructs a new InvalidOtpException with a custom message.
     *
     * @param message custom description of the error
     */
    public InvalidOtpException(String message) {
        super(Error.INVALID_OTP, message);
    }

    /**
     * Constructs a new InvalidOtpException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public InvalidOtpException(String message, Throwable cause) {
        super(Error.INVALID_OTP, message);
        initCause(cause);
    }
}
