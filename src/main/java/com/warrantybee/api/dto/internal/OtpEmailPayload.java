package com.warrantybee.api.dto.internal;

import com.warrantybee.api.enumerations.OtpRequestReason;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.Map;

/**
 * Represents the data payload used for sending an OTP email.
 */
@Getter
@Setter
@AllArgsConstructor
public class OtpEmailPayload {

    /** Recipient's email address. */
    private String email;

    /** The generated one-time password. */
    private String otp;

    /** The reason for which the OTP is being sent. */
    private OtpRequestReason reason;

    /** Dynamic macros to be replaced in the email template. */
    private Map<String, String> dynamicMacros;
}
