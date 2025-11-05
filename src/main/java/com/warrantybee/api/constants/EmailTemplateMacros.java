package com.warrantybee.api.constants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.util.Map;

/**
 * Holds and manages macros used in email templates.
 */
public class EmailTemplateMacros {

    @Autowired
    private Environment _environment;

    private EmailTemplateMacros() { }

    /** Map storing macro key-value pairs. */
    private Map<String, String> _macros = Map.of(
        "ORGANIZATION_NAME", _environment.getProperty("WARRANTYBEE_MACRO_ORGANIZATION_NAME"),
        "SUPPORT_EMAIL", _environment.getProperty("WARRANTYBEE_MACRO_SUPPORT_EMAIL"),
        "PRIVACY_POLICY_URL", _environment.getProperty("WARANTYBEE_MACRO_PRIVACY_POLICY_URL")
    );

    /**
     * Retrieves the value of a macro by its key.
     *
     * @param key the macro key
     * @return the macro value, or {@code null} if not found
     */
    public static String get(String key) {
        EmailTemplateMacros obj = new EmailTemplateMacros();
        return obj._macros.getOrDefault(key, null);
    }
}
