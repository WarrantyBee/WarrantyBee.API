package com.warrantybee.api.services.interfaces;

import com.warrantybee.api.dto.internal.EmailPayload;
import com.warrantybee.api.dto.internal.OtpEmailPayload;

/** Service interface for sending emails. */
public interface IEmailService {

    /**
     * Sends an email with the specified payload.
     * @param payload the email details including recipients, body, and attachments
     */
    void send(EmailPayload payload);

    /**
     * Sends an OTP email to the specified recipient based on the provided payload.
     * @param payload the OTP email payload containing recipient details, OTP, reason, and dynamic macros
     */
    void sendOtp(OtpEmailPayload payload);
}
