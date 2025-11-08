package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the OTP recipient is required.
 */
public class OtpRecipientRequiredException extends APIException {

    /**
     * Constructs a new OtpRecipientRequiredException with the default message from {@link Error#OTP_RECIPIENT_REQUIRED}.
     */
    public OtpRecipientRequiredException() {
        super(Error.OTP_RECIPIENT_REQUIRED);
    }

    /**
     * Constructs a new OtpRecipientRequiredException with a custom message.
     *
     * @param message custom description of the error
     */
    public OtpRecipientRequiredException(String message) {
        super(Error.OTP_RECIPIENT_REQUIRED, message);
    }

    /**
     * Constructs a new OtpRecipientRequiredException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public OtpRecipientRequiredException(String message, Throwable cause) {
        super(Error.OTP_RECIPIENT_REQUIRED, message);
        initCause(cause);
    }
}