package com.warrantybee.api.services.implementations;

import com.warrantybee.api.exceptions.EmailTemplateNotFoundException;
import com.warrantybee.api.exceptions.EmailTemplateParsingException;
import com.warrantybee.api.services.interfaces.IEmailTemplateService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Implementation of {@link IEmailTemplateService} for processing email templates.
 */
@Service
public class EmailTemplateService implements IEmailTemplateService {

    @Override
    public String process(String templatePath, Map<String, String> macros) {
        String content = null;

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(templatePath)) {
            if (inputStream == null) {
                throw new EmailTemplateNotFoundException();
            }
            else {
                content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            throw new EmailTemplateParsingException();
        }


        for (Map.Entry<String, String> entry : macros.entrySet()) {
            String macro = "<macro>" + entry.getKey() + "</macro>";
            content = content.replace(macro, entry.getValue());
        }

        return content;
    }
}
