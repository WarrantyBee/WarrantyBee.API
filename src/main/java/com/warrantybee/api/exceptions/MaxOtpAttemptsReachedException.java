package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the maximum OTP attempts reached.
 */
public class MaxOtpAttemptsReachedException extends APIException {

    /**
     * Constructs a new MaxOtpAttemptsReachedException with the default message from {@link Error#MAX_OTP_ATTEMPTS_REACHED}.
     */
    public MaxOtpAttemptsReachedException() {
        super(Error.MAX_OTP_ATTEMPTS_REACHED);
    }

    /**
     * Constructs a new MaxOtpAttemptsReachedException with a custom message.
     *
     * @param message custom description of the error
     */
    public MaxOtpAttemptsReachedException(String message) {
        super(Error.MAX_OTP_ATTEMPTS_REACHED, message);
    }

    /**
     * Constructs a new MaxOtpAttemptsReachedException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public MaxOtpAttemptsReachedException(String message, Throwable cause) {
        super(Error.MAX_OTP_ATTEMPTS_REACHED, message);
        initCause(cause);
    }
}
