package com.warrantybee.api.config;

import com.warrantybee.api.configurations.AppConfiguration;
import com.warrantybee.api.configurations.ReCaptchaConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReCaptchaConfig {

    private final AppConfiguration appConfiguration;

    public ReCaptchaConfig(AppConfiguration appConfiguration) {
        this.appConfiguration = appConfiguration;
    }

    @Bean
    public ReCaptchaConfiguration reCaptchaConfiguration() {
        return appConfiguration.getReCaptchaConfiguration();
    }
}
