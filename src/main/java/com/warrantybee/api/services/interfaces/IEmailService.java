package com.warrantybee.api.services.interfaces;

import com.warrantybee.api.dto.internal.EmailPayload;

/** Service interface for sending emails. */
public interface IEmailService {

    /**
     * Sends an email with the specified payload.
     * @param payload the email details including recipients, body, and attachments
     */
    void send(EmailPayload payload);

    /**
     * Sends an OTP email to the specified address.
     * @param email the recipient's email address
     * @param otp   the one-time password to send
     */
    void sendOtp(String email, String otp);
}
