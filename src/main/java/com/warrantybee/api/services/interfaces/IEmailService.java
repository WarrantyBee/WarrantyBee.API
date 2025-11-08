package com.warrantybee.api.services.interfaces;

import com.warrantybee.api.dto.internal.EmailPayload;
import com.warrantybee.api.dto.internal.OtpEmailPayload;
import com.warrantybee.api.dto.request.SignUpRequest;

/** Service interface for sending emails. */
public interface IEmailService {

    /**
     * Sends an email with the specified payload.
     * @param payload the email details including recipients, body, and attachments
     */
    void send(EmailPayload payload);

    /**
     * Sends a welcome email to the user based on the signup request.
     *
     * @param request the signup request containing user details
     */
    void sendWelcomeMail(SignUpRequest request);

    /**
     * Sends an OTP email to the specified recipient based on the provided payload.
     * @param payload the OTP email payload containing recipient details, OTP, reason, and dynamic macros
     */
    void sendOtp(OtpEmailPayload payload);
}
