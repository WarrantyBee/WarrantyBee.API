package com.warrantybee.api.config;

import com.warrantybee.api.configurations.AppConfiguration;
import com.warrantybee.api.configurations.ReCaptchaConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration class responsible for exposing the ReCaptcha settings
 * from the main application configuration as a standalone bean.
 */
@Configuration
public class ReCaptchaConfig {

    private final AppConfiguration appConfiguration;

    /**
     * Constructs the configuration, injecting the primary application settings.
     * @param appConfiguration The overall application configuration object.
     */
    public ReCaptchaConfig(AppConfiguration appConfiguration) {
        this.appConfiguration = appConfiguration;
    }

    /**
     * Creates a {@link ReCaptchaConfiguration} bean by extracting the ReCaptcha
     * specific settings from the main {@link AppConfiguration}.
     *
     * @return The configured {@link ReCaptchaConfiguration} instance.
     */
    @Bean
    public ReCaptchaConfiguration reCaptchaConfiguration() {
        return appConfiguration.getRecaptchaConfiguration();
    }
}
