package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when an error occurs during email template parsing.
 */
public class EmailTemplateParsingException extends APIException {

    /**
     * Constructs a new EmailTemplateParsingException with the default message from {@link Error#EMAIL_TEMPLATE_PARSING_ERROR}.
     */
    public EmailTemplateParsingException() {
        super(Error.EMAIL_TEMPLATE_PARSING_ERROR);
    }

    /**
     * Constructs a new EmailTemplateParsingException with a custom message.
     *
     * @param message custom description of the error
     */
    public EmailTemplateParsingException(String message) {
        super(Error.EMAIL_TEMPLATE_PARSING_ERROR, message);
    }

    /**
     * Constructs a new EmailTemplateParsingException with a custom message and cause.
     *
     * @param message custom description of the error
     * @param cause   the underlying cause of the exception
     */
    public EmailTemplateParsingException(String message, Throwable cause) {
        super(Error.EMAIL_TEMPLATE_PARSING_ERROR, message);
        initCause(cause);
    }
}
