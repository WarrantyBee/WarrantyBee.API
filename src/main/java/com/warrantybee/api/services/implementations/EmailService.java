package com.warrantybee.api.services.implementations;

import com.warrantybee.api.configurations.AppConfiguration;
import com.warrantybee.api.constants.EmailSubject;
import com.warrantybee.api.constants.EmailTemplate;
import com.warrantybee.api.constants.EmailTemplateMacros;
import com.warrantybee.api.dto.internal.EmailPayload;
import com.warrantybee.api.dto.internal.OtpEmailPayload;
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
    public void sendOtp(OtpEmailPayload payload) {
        Map<String, String> macros = _getMacros(payload);
        EmailPayload emailPayload = _getPayload(payload, macros);
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
        macros.put("ORGANIZATION_NAME", EmailTemplateMacros.get("ORGANIZATION_NAME"));
        macros.put("OTP", payload.getOtp());
        macros.put("EXPIRY_TIME", _expirationMins.toString());
        macros.put("SUPPORT_EMAIL", EmailTemplateMacros.get("SUPPORT_EMAIL"));
        macros.put("PRIVACY_POLICY_URL", EmailTemplateMacros.get("PRIVACY_POLICY_URL"));

        if (payload.getDynamicMacros() != null) {
            for (Map.Entry<String, String> entry : payload.getDynamicMacros().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                macros.put(key, value);
            }
        }

        return macros;
    }

    /**
     * Creates and returns an {@link EmailPayload} object based on the OTP reason and macros.
     * Processes the appropriate email template and sets subject, body, and recipient.
     *
     * @param payload the OTP email payload containing recipient and reason
     * @param macros the macros to be used in the email body
     * @return a fully prepared {@code EmailPayload} ready to be sent
     */
    private EmailPayload _getPayload(OtpEmailPayload payload, Map<String, String> macros) {
        EmailPayload emailPayload = new EmailPayload();
        String body = null;

        switch (payload.getReason()) {
            case OtpRequestReason.Login -> {
                body = _templateService.process(EmailTemplate.LOGIN, macros);
                emailPayload.setSubject(EmailSubject.LOGIN);
            }
            case OtpRequestReason.ForgotPassword -> {
                body = _templateService.process(EmailTemplate.FORGOT_PASSWORD, macros);
                emailPayload.setSubject(EmailSubject.FORGOT_PASSWORD);
            }
        }

        emailPayload.setBody(body);
        emailPayload.setTo(new String[] { payload.getEmail() });
        return emailPayload;
    }
}
