package com.warrantybee.api.configurations;

import lombok.Getter;
import lombok.Setter;

/**
 * Configuration class for defining properties related to Google reCAPTCHA
 * verification service.
 */
@Getter
@Setter
public class ReCaptchaConfiguration {

    /** The secret key used to communicate with the Google reCAPTCHA API. */
    private String secret;

    /** The endpoint URL used to verify a reCAPTCHA response token. */
    private String verifyUrl;

    /** The minimum acceptable score for reCAPTCHA v3 verification (optional). */
    private double minimumScore;
}
