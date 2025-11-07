package com.warrantybee.api.config;

import com.warrantybee.api.configurations.AppConfiguration;
import com.warrantybee.api.configurations.SmtpConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class SmtpConfig {
    private final SmtpConfiguration smtpConfiguration;

    public SmtpConfig(AppConfiguration appConfiguration) {
        this.smtpConfiguration = appConfiguration.getSmtpConfiguration();
    }
}
