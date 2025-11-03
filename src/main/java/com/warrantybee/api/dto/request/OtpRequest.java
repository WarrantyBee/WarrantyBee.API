package com.warrantybee.api.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a request for OTP generation or verification.
 */
@Getter
@Setter
public class OtpRequest {
    /** User ID associated with the request */
    private Long userId;

    /** Email address of the user */
    private String email;
}
