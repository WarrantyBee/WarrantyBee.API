package com.warrantybee.api.dto.request;

import com.warrantybee.api.dto.request.interfaces.ILoginRequest;
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
public class LoginRequest implements ILoginRequest {

    /** The user's email address, which serves as the login identifier. */
    private String email;

    /** The user's plain-text password. */
    private String password;

    /** The verification token received from the CAPTCHA service (e.g., reCAPTCHA). */
    private String captchaResponse;
}
