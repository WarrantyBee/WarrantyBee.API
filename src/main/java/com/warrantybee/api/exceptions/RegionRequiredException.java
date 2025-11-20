package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the region is not specified.
 */
public class RegionRequiredException extends APIException {

    /**
     * Constructs a new RegionRequiredException with the default message from {@link Error#REGION_REQUIRED}.
     */
    public RegionRequiredException() {
        super(Error.REGION_REQUIRED);
    }

    /**
     * Constructs a new RegionRequiredException with a custom message.
     *
     * @param message custom description of the error
     */
    public RegionRequiredException(String message) {
        super(Error.REGION_REQUIRED, message);
    }

    /**
     * Constructs a new RegionRequiredException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public RegionRequiredException(String message, Throwable cause) {
        super(Error.REGION_REQUIRED, message);
        initCause(cause);
    }
}
