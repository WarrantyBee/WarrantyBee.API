package com.warrantybee.api.services.implementations;

import com.warrantybee.api.configurations.AppConfiguration;
import com.warrantybee.api.constants.EmailSubject;
import com.warrantybee.api.constants.EmailTemplate;
import com.warrantybee.api.constants.EmailTemplateMacros;
import com.warrantybee.api.dto.internal.EmailPayload;
import com.warrantybee.api.dto.internal.NotificationPayload;
import com.warrantybee.api.enumerations.NotificationType;
import com.warrantybee.api.services.interfaces.IEmailService;
import com.warrantybee.api.services.interfaces.IEmailTemplateService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link IEmailService} responsible for composing and sending emails.
 * <p>
 * This service handles template processing, macro substitution, and delivery of
 * different types of notification and OTP emails.
 * </p>
 */
@Service
public class EmailService implements IEmailService {

    private final IEmailTemplateService _templateService;
    private final Integer _expirationMins;
    private final JavaMailSender _sender;

    /**
     * Initializes the {@code EmailService} with configuration and dependencies.
     *
     * @param configuration   application configuration containing OTP settings
     * @param templateService service used for processing email templates
     * @param sender          JavaMail sender for dispatching email messages
     */
    public EmailService(AppConfiguration configuration, IEmailTemplateService templateService, JavaMailSender sender) {
        var otpConfiguration = configuration.getOtpConfiguration();
        _expirationMins = otpConfiguration.getExpiration();
        this._templateService = templateService;
        this._sender = sender;
    }

    @Override
    public void send(NotificationPayload notification) {
        try {
            EmailPayload payload = _getPayload(notification);
            MimeMessage message = _sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(payload.getTo());
            if (payload.getCc() != null && payload.getCc().length > 0) {
                helper.setCc(payload.getCc());
            }
            if (payload.getBcc() != null && payload.getBcc().length > 0) {
                helper.setBcc(payload.getBcc());
            }
            helper.setSubject(payload.getSubject());
            helper.setText(payload.getBody(), true);

            _sender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Prepares a map of macros for email templates by combining
     * dynamic and common macro values.
     *
     * @param payload the notification payload containing dynamic macros
     * @return a populated macro map for template rendering
     */
    private Map<String, String> _getMacros(NotificationPayload payload) {
        Map<String, String> macros = new HashMap<>();

        if (payload.getDynamicMacros() != null) {
            macros.putAll(payload.getDynamicMacros());
        }

        _getCommonMacros(macros);
        return macros;
    }

    /**
     * Adds global organization-related macros such as support email,
     * privacy policy URL, and expiry time to the provided map.
     *
     * @param macros the macro map to populate
     */
    private void _getCommonMacros(Map<String, String> macros) {
        macros.put("ORGANIZATION_NAME", EmailTemplateMacros.get("ORGANIZATION_NAME"));
        macros.put("SUPPORT_EMAIL", EmailTemplateMacros.get("SUPPORT_EMAIL"));
        macros.put("PRIVACY_POLICY_URL", EmailTemplateMacros.get("PRIVACY_POLICY_URL"));
        macros.put("LOG_IN_URL", EmailTemplateMacros.get("LOG_IN_URL"));
        macros.put("EXPIRY_TIME", _expirationMins.toString());
    }

    /**
     * Builds an {@link EmailPayload} based on the notification type and associated macros.
     *
     * @param payload the notification data containing recipient and type
     * @return a fully populated {@link EmailPayload} ready to send
     */
    private EmailPayload _getPayload(NotificationPayload payload) {
        EmailPayload emailPayload = new EmailPayload();
        Map<String, String> macros = _getMacros(payload);
        String body = null;

        switch (payload.getType()) {
            case NotificationType.MFA_LOGIN -> {
                body = _templateService.process(EmailTemplate.LOGIN, macros);
                emailPayload.setSubject(EmailSubject.LOGIN);
            }
            case NotificationType.FORGOT_PASSWORD -> {
                body = _templateService.process(EmailTemplate.FORGOT_PASSWORD, macros);
                emailPayload.setSubject(EmailSubject.FORGOT_PASSWORD);
            }
            case NotificationType.WELCOME -> {
                body = _templateService.process(EmailTemplate.NEW_ACCOUNT, macros);
                emailPayload.setSubject(EmailSubject.NEW_ACCOUNT);
            }
            case NotificationType.PASSWORD_CHANGED -> {
                body = _templateService.process(EmailTemplate.PASSWORD_CHANGED, macros);
                emailPayload.setSubject(EmailSubject.PASSWORD_CHANGED);
            }
        }

        emailPayload.setBody(body);
        emailPayload.setTo(new String[] { payload.getRecipient() });
        return emailPayload;
    }
}
