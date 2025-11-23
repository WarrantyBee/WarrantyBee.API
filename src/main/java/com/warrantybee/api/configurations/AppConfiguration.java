package com.warrantybee.api.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Loads application-wide configuration settings from properties files
 * (prefixed by 'app'). This class aggregates various configuration groups
 * used throughout the application.
 */
@Configuration
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AppConfiguration {

    /** The display name of the application. */
    private String name;

    /** The runtime environment (e.g., dev, prod, staging). */
    private String environment;

    /** Configuration properties for the database connection. */
    private DataSourceConfiguration dataSourceConfiguration;

    /** Configuration properties for the Upstash Redis cache service. */
    private UpstashConfiguration upstashConfiguration;

    /** Configuration properties for Better Stack logging and monitoring. */
    private BetterStackConfiguration betterStackConfiguration;

    /** Configuration properties related to JWT token generation and validation. */
    private JwtTokenConfiguration jwtTokenConfiguration;

    /** Configuration properties for Google reCaptcha validation service. */
    private ReCaptchaConfiguration recaptchaConfiguration;

    /** Configuration properties for the SMTP (email) service used to send emails such as OTPs or notifications. */
    private SmtpConfiguration smtpConfiguration;

    /** Configuration properties for OTP (One-Time Password) settings such as expiration time or length. */
    private OtpConfiguration otpConfiguration;

    /** Configuration properties related to user profile management. */
    private ProfileConfiguration profileConfiguration;

    /** Configuration properties related to Cloudinary service. */
    private CloudinaryConfiguration cloudinaryConfiguration;
}
