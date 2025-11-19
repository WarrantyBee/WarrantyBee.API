package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the phone code is invalid.
 */
public class InvalidPhoneCodeException extends APIException {

    /**
     * Constructs a new InvalidPhoneCodeException with the default message from {@link Error#INVALID_PHONE_CODE}.
     */
    public InvalidPhoneCodeException() {
        super(Error.INVALID_PHONE_CODE);
    }

    /**
     * Constructs a new InvalidPhoneCodeException with a custom message.
     *
     * @param message custom description of the error
     */
    public InvalidPhoneCodeException(String message) {
        super(Error.INVALID_PHONE_CODE, message);
    }

    /**
     * Constructs a new InvalidPhoneCodeException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public InvalidPhoneCodeException(String message, Throwable cause) {
        super(Error.INVALID_PHONE_CODE, message);
        initCause(cause);
    }
}
