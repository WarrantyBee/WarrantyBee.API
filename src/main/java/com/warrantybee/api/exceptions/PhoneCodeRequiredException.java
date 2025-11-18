package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the phone code is required.
 */
public class PhoneCodeRequiredException extends APIException {

    /**
     * Constructs a new PhoneCodeRequiredException with the default message from {@link Error#PHONE_CODE_REQUIRED}.
     */
    public PhoneCodeRequiredException() {
        super(Error.PHONE_CODE_REQUIRED);
    }

    /**
     * Constructs a new PhoneCodeRequiredException with a custom message.
     *
     * @param message custom description of the error
     */
    public PhoneCodeRequiredException(String message) {
        super(Error.PHONE_CODE_REQUIRED, message);
    }

    /**
     * Constructs a new PhoneCodeRequiredException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public PhoneCodeRequiredException(String message, Throwable cause) {
        super(Error.PHONE_CODE_REQUIRED, message);
        initCause(cause);
    }
}
