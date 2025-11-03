package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the OTP receiver is required.
 */
public class OtpReceiverRequiredException extends APIException {

    /**
     * Constructs a new OtpReceiverRequiredException with the default message from {@link Error#OTP_RECEIVER_REQUIRED}.
     */
    public OtpReceiverRequiredException() {
        super(Error.OTP_RECEIVER_REQUIRED);
    }

    /**
     * Constructs a new OtpReceiverRequiredException with a custom message.
     *
     * @param message custom description of the error
     */
    public OtpReceiverRequiredException(String message) {
        super(Error.OTP_RECEIVER_REQUIRED, message);
    }

    /**
     * Constructs a new OtpReceiverRequiredException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public OtpReceiverRequiredException(String message, Throwable cause) {
        super(Error.OTP_RECEIVER_REQUIRED, message);
        initCause(cause);
    }
}
