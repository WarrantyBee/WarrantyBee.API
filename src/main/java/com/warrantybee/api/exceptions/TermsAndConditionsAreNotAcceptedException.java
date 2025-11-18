package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the terms and conditions are not accepted.
 */
public class TermsAndConditionsAreNotAcceptedException extends APIException {

    /**
     * Constructs a new TermsAndConditionsAreNotAcceptedException with the default message from {@link Error#TERMS_AND_CONDITIONS_NOT_ACCEPTED}.
     */
    public TermsAndConditionsAreNotAcceptedException() {
        super(Error.TERMS_AND_CONDITIONS_NOT_ACCEPTED);
    }

    /**
     * Constructs a new TermsAndConditionsAreNotAcceptedException with a custom message.
     *
     * @param message custom description of the error
     */
    public TermsAndConditionsAreNotAcceptedException(String message) {
        super(Error.TERMS_AND_CONDITIONS_NOT_ACCEPTED, message);
    }

    /**
     * Constructs a new TermsAndConditionsAreNotAcceptedException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public TermsAndConditionsAreNotAcceptedException(String message, Throwable cause) {
        super(Error.TERMS_AND_CONDITIONS_NOT_ACCEPTED, message);
        initCause(cause);
    }
}
