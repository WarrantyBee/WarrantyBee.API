package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the provided postal code is invalid.
 */
public class InvalidPostalCodeException extends APIException {

    /**
     * Constructs a new InvalidPostalCodeException with the default message from {@link Error#INVALID_POSTAL_CODE}.
     */
    public InvalidPostalCodeException() {
        super(Error.INVALID_POSTAL_CODE);
    }

    /**
     * Constructs a new InvalidPostalCodeException with a custom message.
     *
     * @param message custom description of the error
     */
    public InvalidPostalCodeException(String message) {
        super(Error.INVALID_POSTAL_CODE, message);
    }

    /**
     * Constructs a new InvalidPostalCodeException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public InvalidPostalCodeException(String message, Throwable cause) {
        super(Error.INVALID_POSTAL_CODE, message);
        initCause(cause);
    }
}
