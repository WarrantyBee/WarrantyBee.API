package com.warrantybee.api.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the filter criteria for searching OTP records.
 */
@Getter
@Setter
@AllArgsConstructor
public class OtpSearchFilter {

    /** Email or phone number of the OTP recipient. */
    private String recipient;

    /** Unique ID of the OTP recipient. */
    private Long recipientId;

    /** Reason code indicating why the OTP was requested. */
    private Byte reason;
}
