package com.warrantybee.api.dto.request;

import com.warrantybee.api.dto.request.interfaces.ILoginRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a request payload for multi-factor authentication (MFA) login.
 * Contains user credentials and verification details.
 */
@Getter
@Setter
@AllArgsConstructor
public class MFALoginRequest implements ILoginRequest {
    /** User's email address used for login. */
    private String email;

    /** The user's plain-text password. */
    private String password;

    /** Temporary token issued during the MFA process. */
    private String token;

    /** One-time password (OTP) provided for authentication. */
    private String otp;

    /** The verification token received from the CAPTCHA service (e.g., reCAPTCHA). */
    private String captchaResponse;
}
