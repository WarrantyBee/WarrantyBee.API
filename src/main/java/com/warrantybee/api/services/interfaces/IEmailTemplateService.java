package com.warrantybee.api.services.interfaces;

import java.util.Map;

/**
 * Service interface for processing and rendering email templates.
 */
public interface IEmailTemplateService {

    /**
     * Processes the given email template by replacing macros with actual values.
     *
     * @param templatePath the path to the email template file
     * @param macros a map containing macro keys and their replacement values
     * @return the processed email content as a string
     */
    String process(String templatePath, Map<String, String> macros);
}
