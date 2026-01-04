package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when a vendor's access is denied.
 */
public class VendorAccessDeniedException extends APIException {

    /**
     * Constructs a new VendorAccessDeniedException with the default message from {@link Error#VENDOR_ACCESS_DENIED}.
     */
    public VendorAccessDeniedException() {
        super(Error.VENDOR_ACCESS_DENIED);
    }

    /**
     * Constructs a new VendorAccessDeniedException with a custom message.
     *
     * @param message custom description of the error
     */
    public VendorAccessDeniedException(String message) {
        super(Error.VENDOR_ACCESS_DENIED, message);
    }

    /**
     * Constructs a new VendorAccessDeniedException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public VendorAccessDeniedException(String message, Throwable cause) {
        super(Error.VENDOR_ACCESS_DENIED, message);
        initCause(cause);
    }
}
