package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the address is required but not provided.
 */
public class AddressRequiredException extends APIException {

    /**
     * Constructs a new AddressRequiredException with the default message from {@link Error#ADDRESS_REQUIRED}.
     */
    public AddressRequiredException() {
        super(Error.ADDRESS_REQUIRED);
    }

    /**
     * Constructs a new AddressRequiredException with a custom message.
     *
     * @param message custom description of the error
     */
    public AddressRequiredException(String message) {
        super(Error.ADDRESS_REQUIRED, message);
    }

    /**
     * Constructs a new AddressRequiredException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public AddressRequiredException(String message, Throwable cause) {
        super(Error.ADDRESS_REQUIRED, message);
        initCause(cause);
    }
}
