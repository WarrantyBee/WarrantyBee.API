package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when the email is required but not provided.
 */
public class EmailRequiredException extends APIException {

    /**
     * Constructs a new EmailRequiredException with the default message from {@link Error#EMAIL_REQUIRED}.
     */
    public EmailRequiredException() {
        super(Error.EMAIL_REQUIRED);
    }

    /**
     * Constructs a new EmailRequiredException with a custom message.
     *
     * @param message custom description of the error
     */
    public EmailRequiredException(String message) {
        super(Error.EMAIL_REQUIRED, message);
    }

    /**
     * Constructs a new EmailRequiredException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public EmailRequiredException(String message, Throwable cause) {
        super(Error.EMAIL_REQUIRED, message);
        initCause(cause);
    }
}
