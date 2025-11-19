package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the privacy policy is not accepted.
 */
public class PrivacyPolicyNotAcceptedException extends APIException {

    /**
     * Constructs a new PrivacyPolicyNotAcceptedException with the default message from {@link Error#PRIVACY_POLICY_NOT_ACCEPTED}.
     */
    public PrivacyPolicyNotAcceptedException() {
        super(Error.PRIVACY_POLICY_NOT_ACCEPTED);
    }

    /**
     * Constructs a new PrivacyPolicyNotAcceptedException with a custom message.
     *
     * @param message custom description of the error
     */
    public PrivacyPolicyNotAcceptedException(String message) {
        super(Error.PRIVACY_POLICY_NOT_ACCEPTED, message);
    }

    /**
     * Constructs a new PrivacyPolicyNotAcceptedException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public PrivacyPolicyNotAcceptedException(String message, Throwable cause) {
        super(Error.PRIVACY_POLICY_NOT_ACCEPTED, message);
        initCause(cause);
    }
}
