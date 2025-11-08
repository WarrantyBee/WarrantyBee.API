package com.warrantybee.api.dto.request;

import com.warrantybee.api.enumerations.OtpRequestReason;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a request for OTP generation or verification.
 */
@Getter
@Setter
@AllArgsConstructor
public class OtpRequest {
    /** User ID associated with the request */
    private Long userId;

    /** Email address of the user */
    private String email;

    /** Reason for requesting OTP */
    private OtpRequestReason reason;
}
