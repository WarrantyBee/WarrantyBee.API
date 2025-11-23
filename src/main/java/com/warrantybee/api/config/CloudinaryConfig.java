package com.warrantybee.api.config;

import com.cloudinary.Cloudinary;
import com.warrantybee.api.configurations.AppConfiguration;
import com.warrantybee.api.configurations.CloudinaryConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class responsible for initializing and providing the Cloudinary client
 * as a Spring-managed bean.
 */
@Configuration
public class CloudinaryConfig {

    private final AppConfiguration _appConfiguration;

    /**
     * Constructs a new {@code CloudinaryConfig} instance.
     * @param appConfiguration the application-level configuration containing Cloudinary settings
     */
    public CloudinaryConfig(AppConfiguration appConfiguration) {
        this._appConfiguration = appConfiguration;
    }

    /**
     * Creates and exposes a {@link Cloudinary} bean initialized with the credentials
     * provided in the application's configuration.
     * @return a configured {@link Cloudinary} instance
     */
    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        CloudinaryConfiguration cloudinaryConfiguration = _appConfiguration.getCloudinaryConfiguration();

        config.put("cloud_name", cloudinaryConfiguration.getCloud());
        config.put("api_key", cloudinaryConfiguration.getApiKey());
        config.put("api_secret", cloudinaryConfiguration.getApiSecret());

        return new Cloudinary(config);
    }
}
