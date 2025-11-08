package com.warrantybee.api.services.implementations;

import com.warrantybee.api.configurations.AppConfiguration;
import com.warrantybee.api.constants.EmailSubject;
import com.warrantybee.api.constants.EmailTemplate;
import com.warrantybee.api.constants.EmailTemplateMacros;
import com.warrantybee.api.dto.internal.EmailPayload;
import com.warrantybee.api.dto.internal.OtpEmailPayload;
import com.warrantybee.api.dto.internal.UserCreationRequest;
import com.warrantybee.api.dto.request.SignUpRequest;
import com.warrantybee.api.enumerations.OtpRequestReason;
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
 * Service implementation for handling email operations such as sending messages and OTPs.
 */
@Service
public class EmailService implements IEmailService {

    private final IEmailTemplateService _templateService;
    private final Integer _expirationMins;
    private final JavaMailSender _sender;

    public EmailService(AppConfiguration configuration, IEmailTemplateService templateService, JavaMailSender sender) {
        var otpConfiguration = configuration.getOtpConfiguration();
        _expirationMins = otpConfiguration.getExpiration();
        this._templateService = templateService;
        this._sender = sender;
    }

    @Override
    public void send(EmailPayload payload) {
        try {
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

    @Override
    public void sendWelcomeMail(SignUpRequest request) {
        Map<String, String> macros = _getMacros(request);
        EmailPayload emailPayload = _getPayload(request.getEmail(), OtpRequestReason.None, macros);
        send(emailPayload);
    }

    @Override
    public void sendOtp(OtpEmailPayload payload) {
        Map<String, String> macros = _getMacros(payload);
        EmailPayload emailPayload = _getPayload(payload.getEmail(), payload.getReason(), macros);
        send(emailPayload);
    }

    /**
     * Builds and returns a map of macros to be used in the OTP email template.
     * Includes default macros and any dynamic ones provided in the payload.
     *
     * @param payload the OTP email payload containing OTP and dynamic macros
     * @return a map of macro key-value pairs for email template processing
     */
    private Map<String, String> _getMacros(OtpEmailPayload payload) {
        Map<String, String> macros = new HashMap<>();
        macros.put("EXPIRY_TIME", _expirationMins.toString());

        if (payload.getDynamicMacros() != null) {
            for (Map.Entry<String, String> entry : payload.getDynamicMacros().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                macros.put(key, value);
            }
        }

        _getCommonMacros(macros);
        return macros;
    }

    /**
     * Generates a map of macros for populating email templates based on the signup request.
     *
     * @param request the signup request containing user details
     * @return a map of macro names and their corresponding values
     */
    private Map<String, String> _getMacros(SignUpRequest request) {
        Map<String, String> macros = new HashMap<>();
        macros.put("LOG_IN_URL", EmailTemplateMacros.get("LOG_IN_URL"));
        macros.put("USER_FIRST_NAME", request.getFirstname());
        macros.put("USER_LAST_NAME", request.getLastname());

        _getCommonMacros(macros);
        return macros;
    }

    /**
     * Adds common organization-related macros to the given map for email templates.
     *
     * @param macros the map to populate with common macro values
     */
    private void _getCommonMacros(Map<String, String> macros) {
        macros.put("ORGANIZATION_NAME", EmailTemplateMacros.get("ORGANIZATION_NAME"));
        macros.put("SUPPORT_EMAIL", EmailTemplateMacros.get("SUPPORT_EMAIL"));
        macros.put("PRIVACY_POLICY_URL", EmailTemplateMacros.get("PRIVACY_POLICY_URL"));
    }

    /**
     * Creates an EmailPayload for the given recipient and OTP request reason.
     *
     * @param recipient the email address of the recipient
     * @param reason the purpose for sending the OTP (e.g., login, forgot password)
     * @param macros key-value pairs to replace placeholders in the email template
     * @return an EmailPayload object with the subject, body, and recipient set
     */
    private EmailPayload _getPayload(String recipient, OtpRequestReason reason, Map<String, String> macros) {
        EmailPayload emailPayload = new EmailPayload();
        String body = null;

        switch (reason) {
            case OtpRequestReason.Login -> {
                body = _templateService.process(EmailTemplate.LOGIN, macros);
                emailPayload.setSubject(EmailSubject.LOGIN);
            }
            case OtpRequestReason.ForgotPassword -> {
                body = _templateService.process(EmailTemplate.FORGOT_PASSWORD, macros);
                emailPayload.setSubject(EmailSubject.FORGOT_PASSWORD);
            }
            case OtpRequestReason.None -> {
                body = _templateService.process(EmailTemplate.NEW_ACCOUNT, macros);
                emailPayload.setSubject(EmailSubject.NEW_ACCOUNT);
            }
        }

        emailPayload.setBody(body);
        emailPayload.setTo(new String[] { recipient });
        return emailPayload;
    }
}
