package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the OTP recipient is invalid.
 */
public class InvalidOtpRecipientException extends APIException {

    /**
     * Constructs a new InvalidOtpRecipientException with the default message from {@link Error#INVALID_OTP_RECIPIENT}.
     */
    public InvalidOtpRecipientException() {
        super(Error.INVALID_OTP_RECIPIENT);
    }

    /**
     * Constructs a new InvalidOtpRecipientException with a custom message.
     *
     * @param message custom description of the error
     */
    public InvalidOtpRecipientException(String message) {
        super(Error.INVALID_OTP_RECIPIENT, message);
    }

    /**
     * Constructs a new InvalidOtpRecipientException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public InvalidOtpRecipientException(String message, Throwable cause) {
        super(Error.INVALID_OTP_RECIPIENT, message);
        initCause(cause);
    }
}
