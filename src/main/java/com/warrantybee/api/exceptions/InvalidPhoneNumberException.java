package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the provided phone number is invalid.
 */
public class InvalidPhoneNumberException extends APIException {

    /**
     * Constructs a new InvalidPhoneNumberException with the default message from {@link Error#INVALID_PHONE_NUMBER}.
     */
    public InvalidPhoneNumberException() {
        super(Error.INVALID_PHONE_NUMBER);
    }

    /**
     * Constructs a new InvalidPhoneNumberException with a custom message.
     *
     * @param message custom description of the error
     */
    public InvalidPhoneNumberException(String message) {
        super(Error.INVALID_PHONE_NUMBER, message);
    }

    /**
     * Constructs a new InvalidPhoneNumberException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public InvalidPhoneNumberException(String message, Throwable cause) {
        super(Error.INVALID_PHONE_NUMBER, message);
        initCause(cause);
    }
}
