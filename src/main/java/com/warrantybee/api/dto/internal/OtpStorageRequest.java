package com.warrantybee.api.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Data transfer object for storing OTP-related information.
 */
@Getter
@Setter
@AllArgsConstructor
public class OtpStorageRequest {

    /**
     * The one-time password (OTP) value to be stored or verified.
     */
    private String value;

    /**
     * The recipient identifier, such as an email address or phone number.
     */
    private String recipient;

    /**
     * The unique ID of the recipient, if available.
     */
    private Long recipientId;
}
