package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when an email template is not found.
 */
public class EmailTemplateNotFoundException extends APIException {

    /**
     * Constructs a new EmailTemplateNotFoundException with the default message from {@link Error#EMAIL_TEMPLATE_NOT_FOUND}.
     */
    public EmailTemplateNotFoundException() {
        super(Error.EMAIL_TEMPLATE_NOT_FOUND);
    }

    /**
     * Constructs a new EmailTemplateNotFoundException with a custom message.
     *
     * @param message custom description of the error
     */
    public EmailTemplateNotFoundException(String message) {
        super(Error.EMAIL_TEMPLATE_NOT_FOUND, message);
    }

    /**
     * Constructs a new EmailTemplateNotFoundException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public EmailTemplateNotFoundException(String message, Throwable cause) {
        super(Error.EMAIL_TEMPLATE_NOT_FOUND, message);
        initCause(cause);
    }
}
