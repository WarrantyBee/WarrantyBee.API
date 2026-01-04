package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when an error occurs during vendor contact validation.
 */
public class VendorContactRequiredException extends APIException {
    public VendorContactRequiredException() {
        super(Error.VENDOR_CONTACT_REQUIRED);
    }

    public VendorContactRequiredException(String message) {
        super(Error.VENDOR_CONTACT_REQUIRED, message);
    }

    public VendorContactRequiredException(String message, Throwable cause) {
        super(Error.VENDOR_CONTACT_REQUIRED, message);
        initCause(cause);
    }
}
