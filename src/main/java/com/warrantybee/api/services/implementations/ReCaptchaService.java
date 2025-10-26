package com.warrantybee.api.services.implementations;

import com.warrantybee.api.configurations.ReCaptchaConfiguration;
import com.warrantybee.api.exceptions.CaptchaServiceException;
import com.warrantybee.api.exceptions.ConfigurationException;
import com.warrantybee.api.exceptions.InvalidInputException;
import com.warrantybee.api.services.interfaces.ICaptchaService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Service for validating Google reCAPTCHA responses.
 */
@Service
public class ReCaptchaService implements ICaptchaService {

    private final ReCaptchaConfiguration _reCaptchaConfiguration;
    private final RestTemplate _restTemplate;

    /**
     * Constructs a new {@code ReCaptchaService} with the specified configuration.
     * Initializes a {@link RestTemplate} instance and validates that the
     * reCAPTCHA verification URL and secret key are properly configured.
     *
     * @param reCaptchaConfiguration the configuration settings for reCAPTCHA verification
     * @throws ConfigurationException if the verification URL or secret key is missing
     */
    public ReCaptchaService(ReCaptchaConfiguration reCaptchaConfiguration) {
        this._reCaptchaConfiguration = reCaptchaConfiguration;
        this._restTemplate = new RestTemplate();

        if (reCaptchaConfiguration.getVerifyUrl() == null || reCaptchaConfiguration.getSecret() == null) {
            throw new ConfigurationException("ReCaptcha verify URL or secret is not configured");
        }
    }

    /**
     * Validates the provided reCAPTCHA response token with Google API.
     *
     * @param captchaResponse the response token received from client
     * @return true if valid, false otherwise
     */
    @Override
    public boolean validate(String captchaResponse) {
        try {
            String verifyUrl = _reCaptchaConfiguration.getVerifyUrl() +
                    "?secret=" + _reCaptchaConfiguration.getSecret() +
                    "&response=" + captchaResponse;

            ResponseEntity<Map> response = _restTemplate.exchange(
                    verifyUrl, HttpMethod.POST, HttpEntity.EMPTY, Map.class);

            Map<String, Object> body = response.getBody();
            if (body == null || !Boolean.TRUE.equals(body.get("success"))) {
                throw new InvalidInputException("Invalid captcha response");
            }

            if (body.containsKey("score")) {
                double score = ((Number) body.get("score")).doubleValue();
                return score >= _reCaptchaConfiguration.getMinimumScore();
            }

            return true;
        } catch (Exception e) {
            throw new CaptchaServiceException("Failed to validate captcha", e);
        }
    }
}