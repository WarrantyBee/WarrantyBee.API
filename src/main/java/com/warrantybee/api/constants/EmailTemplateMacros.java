package com.warrantybee.api.constants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * Provides access to predefined macros used in email templates.
 */
@Component
public class EmailTemplateMacros {

    private static Map<String, String> macros;

    /**
     * Initializes the macro map using environment properties.
     *
     * @param environment the Spring environment containing macro property values
     */
    @Autowired
    public EmailTemplateMacros(Environment environment) {
        macros = Map.of(
            "ORGANIZATION_NAME", Objects.requireNonNull(environment.getProperty("WARRANTYBEE_MACRO_ORG_NAME")),
            "SUPPORT_EMAIL", Objects.requireNonNull(environment.getProperty("WARRANTYBEE_MACRO_SUPPORT_EMAIL")),
            "PRIVACY_POLICY_URL", Objects.requireNonNull(environment.getProperty("WARRANTYBEE_MACRO_PRIVACY_POLICY_URL")),
            "LOG_IN_URL", Objects.requireNonNull(environment.getProperty("WARRANTYBEE_MACRO_LOG_IN_URL"))
        );
    }

    /**
     * Retrieves the value of a macro by its key.
     *
     * @param key the macro key
     * @return the macro value, or {@code null} if not found
     */
    public static String get(String key) {
        return macros != null ? macros.getOrDefault(key, null) : null;
    }
}
