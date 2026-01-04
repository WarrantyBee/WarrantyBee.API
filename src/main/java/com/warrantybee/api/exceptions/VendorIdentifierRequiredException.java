package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the vendor identifier is required but not provided.
 */
public class VendorIdentifierRequiredException extends APIException {

    /**
     * Constructs a new VendorIdentifierRequiredException with the default message from {@link Error#VENDOR_IDENTIFIER_REQUIRED}.
     */
    public VendorIdentifierRequiredException() {
        super(Error.VENDOR_IDENTIFIER_REQUIRED);
    }

    /**
     * Constructs a new VendorIdentifierRequiredException with a custom message.
     *
     * @param message custom description of the error
     */
    public VendorIdentifierRequiredException(String message) {
        super(Error.VENDOR_IDENTIFIER_REQUIRED, message);
    }

    /**
     * Constructs a new VendorIdentifierRequiredException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public VendorIdentifierRequiredException(String message, Throwable cause) {
        super(Error.VENDOR_IDENTIFIER_REQUIRED, message);
        initCause(cause);
    }
}
