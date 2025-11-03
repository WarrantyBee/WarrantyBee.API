package com.warrantybee.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for capturing the necessary credentials and verification
 * data when a user attempts to log in.
 */
@Getter
@Setter
@AllArgsConstructor
public class LoginRequest {

    /**
     * The user's email address, which serves as the login identifier.
     * Must be a valid email format and cannot be blank.
     */
    @Email
    @NotBlank
    private String email;

    /**
     * The user's plain-text password.
     * Cannot be blank.
     */
    @NotBlank
    private String password;

    /**
     * The verification token received from the CAPTCHA service (e.g., reCAPTCHA).
     * Cannot be blank.
     */
    @NotBlank
    private String captchaResponse;
}
