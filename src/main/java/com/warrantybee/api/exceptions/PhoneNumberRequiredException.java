package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the phone number is required but not provided.
 */
public class PhoneNumberRequiredException extends APIException {

    /**
     * Constructs a new PhoneNumberRequiredException with the default message from {@link Error#PHONE_NUMBER_REQUIRED}.
     */
    public PhoneNumberRequiredException() {
        super(Error.PHONE_NUMBER_REQUIRED);
    }

    /**
     * Constructs a new PhoneNumberRequiredException with a custom message.
     *
     * @param message custom description of the error
     */
    public PhoneNumberRequiredException(String message) {
        super(Error.PHONE_NUMBER_REQUIRED, message);
    }

    /**
     * Constructs a new PhoneNumberRequiredException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public PhoneNumberRequiredException(String message, Throwable cause) {
        super(Error.PHONE_NUMBER_REQUIRED, message);
        initCause(cause);
    }
}
