package com.warrantybee.api.services.implementations;
import com.warrantybee.api.constants.EmailTemplate;
import com.warrantybee.api.constants.EmailTemplateMacros;
import com.warrantybee.api.dto.internal.EmailPayload;
import com.warrantybee.api.services.interfaces.IEmailService;
import com.warrantybee.api.services.interfaces.IEmailTemplateService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService implements IEmailService {

    @Autowired
    private JavaMailSender _sender;
    private IEmailTemplateService _templateService;

    public EmailService(IEmailTemplateService templateService) {
        this._templateService = templateService;
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
    public void sendOtp(String email, String otp) {
        EmailPayload payload = new EmailPayload();
        Map<String, String> macros = new HashMap<>();
        macros.put("ORGANIZATION_NAME", EmailTemplateMacros.get("ORGANIZATION_NAME"));
        macros.put("OTP", otp);
        macros.put("EXPIRY_TIME", );
        macros.put("SUPPORT_EMAIL", EmailTemplateMacros.get("SUPPORT_EMAIL"));
        macros.put("PRIVACY_POLICY_URL", EmailTemplateMacros.get("PRIVACY_POLICY_URL"));
        String body = _templateService.process(EmailTemplate.OTP, macros);
        payload.setSubject("Your One-Time Password (OTP)");
        payload.setBody(body);
        payload.setTo(new String[] { email });
        send(payload);
    }
}
