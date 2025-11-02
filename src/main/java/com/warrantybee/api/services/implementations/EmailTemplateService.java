package com.warrantybee.api.services.implementations;

import com.warrantybee.api.exceptions.CustomProcessException;
import com.warrantybee.api.services.interfaces.ITemplateService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class EmailTemplateService implements ITemplateService {

    @Override
    public String process(String templatePath, Map<String, String> macros) {

        String templateContent = null;
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(templatePath)) {


            if (inputStream == null) {
                throw new CustomProcessException("Template resource not found or stream is null.");
            }
            templateContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            String errorMessage = "Failed to read template content from input stream.";
            throw new CustomProcessException(errorMessage, new IOException());
        }


        macros.forEach((key, value) -> {
            String template = "<macro>" + key + "</macro>";
            template = template.replace(template, value);
        });
    }
}
