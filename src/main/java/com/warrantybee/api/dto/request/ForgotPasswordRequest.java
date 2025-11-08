package com.warrantybee.api.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a request to initiate the forgot password process.
 */
@Getter
@Setter
public class ForgotPasswordRequest {

    /** Registered email address of the user. */
    private String email;

    /** The verification token received from the CAPTCHA service (e.g., reCAPTCHA). */
    private String captchaResponse;
}
