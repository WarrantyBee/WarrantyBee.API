package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when an error occurs during vendor contact type validation.
 */
public class InvalidVendorContactTypeException extends APIException {
    public InvalidVendorContactTypeException() {
        super(Error.INVALID_VENDOR_CONTACT_TYPE);
    }

    public InvalidVendorContactTypeException(String message) {
        super(Error.INVALID_VENDOR_CONTACT_TYPE, message);
    }

    public InvalidVendorContactTypeException(String message, Throwable cause) {
        super(Error.INVALID_VENDOR_CONTACT_TYPE, message);
        initCause(cause);
    }
}
