package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when an error occurs during OTP generation.
 */
public class OtpGenerationFailedException extends APIException {

    /**
     * Constructs a new OtpGenerationFailedException with the default message from {@link Error#OTP_GENERATION_FAILED}.
     */
    public OtpGenerationFailedException() {
        super(Error.OTP_GENERATION_FAILED);
    }

    /**
     * Constructs a new OtpGenerationFailedException with a custom message.
     *
     * @param message custom description of the OTP generation error
     */
    public OtpGenerationFailedException(String message) {
        super(Error.OTP_GENERATION_FAILED, message);
    }

    /**
     * Constructs a new OtpGenerationFailedException with a custom message and cause.
     *
     * @param message custom description of the OTP generation error
     * @param cause   the underlying cause of the exception
     */
    public OtpGenerationFailedException(String message, Throwable cause) {
        super(Error.OTP_GENERATION_FAILED, message);
        initCause(cause);
    }
}
