package com.warrantybee.api.services.interfaces;

/**
 * Service interface for CAPTCHA verification.
 */
public interface ICaptchaService {

    /**
     * Validates the provided CAPTCHA response token.
     *
     * @param captchaResponse the CAPTCHA response token from client
     * @return true if the CAPTCHA is valid, false otherwise
     */
    boolean validateCaptcha(String captchaResponse);
}
