package com.warrantybee.api.dto.response;

import com.warrantybee.api.enumerations.ComplianceStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * Represents the compliance status of a business entity.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BusinessComplianceResponse {

    /** Current compliance status of the business. */
    private ComplianceStatus status;

    /** Reason or explanation associated with the compliance status, if applicable. */
    private String reason;

    /** Timestamp indicating when the compliance status was last verified. */
    private Timestamp verifiedAt;
}
