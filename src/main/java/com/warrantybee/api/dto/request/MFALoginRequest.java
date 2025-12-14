package com.warrantybee.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a request payload for multifactor authentication (MFA) login.
 * Contains user credentials and verification details.
 */
@Getter
@Setter
@AllArgsConstructor
public class MFALoginRequest extends LoginRequest {
    /** Temporary token issued during the MFA process. */
    private String token;

    /** One-time password (OTP) provided for authentication. */
    private String otp;
}
