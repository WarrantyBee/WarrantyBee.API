package com.warrantybee.api.config;

import com.warrantybee.api.configurations.AppConfiguration;
import com.warrantybee.api.configurations.FacebookConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration class that exposes the Facebook-related settings
 * defined in the main {@link AppConfiguration} as a Spring-managed bean.
 */
@Configuration
public class FacebookConfig {

    private final AppConfiguration appConfiguration;

    /**
     * Initializes the configuration class with the application's primary
     * configuration source.
     *
     * @param appConfiguration the central application configuration containing Facebook settings
     */
    public FacebookConfig(AppConfiguration appConfiguration) {
        this.appConfiguration = appConfiguration;
    }

    /**
     * Creates and registers a {@link FacebookConfiguration} bean by extracting
     * the Facebook-specific configuration properties from {@link AppConfiguration}.
     *
     * @return a populated {@link FacebookConfiguration} instance
     */
    @Bean
    public FacebookConfiguration facebookConfiguration() {
        return appConfiguration.getFacebookConfiguration();
    }
}
