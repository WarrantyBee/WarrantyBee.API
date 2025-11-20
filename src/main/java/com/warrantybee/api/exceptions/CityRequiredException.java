package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the city is required but not provided.
 */
public class CityRequiredException extends APIException {

    /**
     * Constructs a new CityRequiredException with the default message from {@link Error#CITY_REQUIRED}.
     */
    public CityRequiredException() {
        super(Error.CITY_REQUIRED);
    }

    /**
     * Constructs a new CityRequiredException with a custom message.
     *
     * @param message custom description of the error
     */
    public CityRequiredException(String message) {
        super(Error.CITY_REQUIRED, message);
    }

    /**
     * Constructs a new CityRequiredException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public CityRequiredException(String message, Throwable cause) {
        super(Error.CITY_REQUIRED, message);
        initCause(cause);
    }
}
