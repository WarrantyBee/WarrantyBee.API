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

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(this.smtpConfiguration.getHost());
        mailSender.setPort(this.smtpConfiguration.getPort());
        mailSender.setUsername(this.smtpConfiguration.getUsername());
        mailSender.setPassword(this.smtpConfiguration.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "false");

        return mailSender;
    }
}
