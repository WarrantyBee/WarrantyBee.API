package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when an error occurs during vendor contact creation.
 */
public class VendorContactCreationFailedException extends APIException {
    public VendorContactCreationFailedException() {
        super(Error.VENDOR_CONTACT_CREATION_FAILED);
    }

    public VendorContactCreationFailedException(String message) {
        super(Error.VENDOR_CONTACT_CREATION_FAILED, message);
    }

    public VendorContactCreationFailedException(String message, Throwable cause) {
        super(Error.VENDOR_CONTACT_CREATION_FAILED, message);
        initCause(cause);
    }
}
