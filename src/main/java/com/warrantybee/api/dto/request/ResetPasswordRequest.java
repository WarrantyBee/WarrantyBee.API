package com.warrantybee.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a request to reset a user's password using OTP verification.
 */
@Getter
@Setter
@AllArgsConstructor
public class ResetPasswordRequest {

    /** One-time password for verification. */
    private String otp;

    /** Registered email address of the user. */
    private String email;

    /** New password to be set for the user. */
    private String newPassword;

    /** The verification token received from the CAPTCHA service (e.g., reCAPTCHA). */
    private String captchaResponse;
}
