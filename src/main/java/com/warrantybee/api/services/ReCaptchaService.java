package com.warrantybee.api.services;

import com.warrantybee.api.configurations.ReCaptchaConfiguration;
import com.warrantybee.api.services.interfaces.ICaptchaService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import java.util.Map;

/**
 * Service for validating Google reCAPTCHA responses.
 */
@Service
public class ReCaptchaService implements ICaptchaService {

    private final ReCaptchaConfiguration reCaptchaConfiguration;
    private final RestTemplate restTemplate;

    public ReCaptchaService(ReCaptchaConfiguration reCaptchaConfiguration) {
        this.reCaptchaConfiguration = reCaptchaConfiguration;
        this.restTemplate = new RestTemplate();
    }

    /**
     * Validates the provided reCAPTCHA response token with Google API.
     *
     * @param captchaResponse the response token received from client
     * @return true if valid, false otherwise
     */
    @Override
    public boolean validateCaptcha(String captchaResponse) {
        try {
            String verifyUrl = reCaptchaConfiguration.getVerifyUrl() +
                    "?secret=" + reCaptchaConfiguration.getSecret() +
                    "&response=" + captchaResponse;

            ResponseEntity<Map> response = restTemplate.exchange(
                    verifyUrl, HttpMethod.POST, HttpEntity.EMPTY, Map.class);

            Map<String, Object> body = response.getBody();
            if (body == null || !Boolean.TRUE.equals(body.get("success"))) {
                return false;
            }

            if (body.containsKey("score")) {
                double score = ((Number) body.get("score")).doubleValue();
                return score >= reCaptchaConfiguration.getMinimumScore();
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
