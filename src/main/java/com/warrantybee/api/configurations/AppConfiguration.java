package com.warrantybee.api.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Application configuration details.
 */
@Configuration
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AppConfiguration {

    /** App name. */
    private String name;

    /** App environment (e.g., dev, prod). */
    private String environment;

    /** Database settings. */
    private DataSourceConfiguration dataSourceConfiguration;

    /** Upstash Redis settings. */
    private UpstashConfiguration upstashConfiguration;

    /** Better Stack logging settings. */
    private BetterStackConfiguration betterStackConfiguration;

    /** JWT token settings. */
    private JwtTokenConfiguration jwtTokenConfiguration;

    /** reCaptcha settings. */
    private ReCaptchaConfiguration recaptchaConfiguration;
}
