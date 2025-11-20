package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the country is not specified.
 */
public class CountryRequiredException extends APIException {

    /**
     * Constructs a new CountryRequiredException with the default message from {@link Error#COUNTRY_REQUIRED}.
     */
    public CountryRequiredException() {
        super(Error.COUNTRY_REQUIRED);
    }

    /**
     * Constructs a new CountryRequiredException with a custom message.
     *
     * @param message custom description of the error
     */
    public CountryRequiredException(String message) {
        super(Error.COUNTRY_REQUIRED, message);
    }

    /**
     * Constructs a new CountryRequiredException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public CountryRequiredException(String message, Throwable cause) {
        super(Error.COUNTRY_REQUIRED, message);
        initCause(cause);
    }
}
