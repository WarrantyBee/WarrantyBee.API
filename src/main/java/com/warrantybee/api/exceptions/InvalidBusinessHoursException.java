package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the email format is invalid.
 */
public class InvalidBusinessHoursException extends APIException {

    /**
     * Constructs a new InvalidEmailException with the default message from {@link Error#INVALID_BUSINESS_HOURS}.
     */
    public InvalidBusinessHoursException() {
        super(Error.INVALID_BUSINESS_HOURS);
    }

    /**
     * Constructs a new InvalidEmailException with a custom message.
     *
     * @param message custom description of the error
     */
    public InvalidBusinessHoursException(String message) {
        super(Error.INVALID_BUSINESS_HOURS, message);
    }

    /**
     * Constructs a new InvalidEmailException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public InvalidBusinessHoursException(String message, Throwable cause) {
        super(Error.INVALID_BUSINESS_HOURS, message);
        initCause(cause);
    }
}
