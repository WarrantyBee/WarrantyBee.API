package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when MFA is not enabled for the user.
 */
public class MFANotEnabledException extends APIException {

    /**
     * Constructs a new MFANotEnabledException with the default message from {@link Error#MFA_NOT_ENABLED}.
     */
    public MFANotEnabledException() {
        super(Error.MFA_NOT_ENABLED);
    }

    /**
     * Constructs a new MFANotEnabledException with a custom message.
     *
     * @param message custom description of the error
     */
    public MFANotEnabledException(String message) {
        super(Error.MFA_NOT_ENABLED, message);
    }

    /**
     * Constructs a new MFANotEnabledException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public MFANotEnabledException(String message, Throwable cause) {
        super(Error.MFA_NOT_ENABLED, message);
        initCause(cause);
    }
}
