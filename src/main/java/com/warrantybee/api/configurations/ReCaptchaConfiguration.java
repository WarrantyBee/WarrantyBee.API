package com.warrantybee.api.configurations;

import lombok.Getter;
import lombok.Setter;

/**
 * Configuration for Google reCAPTCHA.
 */
@Getter
@Setter
public class ReCaptchaConfiguration {

    /** The secret key provided by Google reCAPTCHA */
    private String secret;

    /** The verification URL to validate CAPTCHA responses */
    private String verifyUrl;

    /** Optional: Minimum score for reCAPTCHA v3 validation */
    private double minimumScore;
}
