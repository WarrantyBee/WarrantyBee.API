package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when a login for a vendor already exists.
 */
public class VendorLoginExistsException extends APIException {

    /**
     * Constructs a new VendorLoginExistsException with the default message from {@link Error#VENDOR_LOGIN_EXISTS}.
     */
    public VendorLoginExistsException() {
        super(Error.VENDOR_LOGIN_EXISTS);
    }

    /**
     * Constructs a new VendorLoginExistsException with a custom message.
     *
     * @param message custom description of the error
     */
    public VendorLoginExistsException(String message) {
        super(Error.VENDOR_LOGIN_EXISTS, message);
    }

    /**
     * Constructs a new VendorLoginExistsException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public VendorLoginExistsException(String message, Throwable cause) {
        super(Error.VENDOR_LOGIN_EXISTS, message);
        initCause(cause);
    }
}
