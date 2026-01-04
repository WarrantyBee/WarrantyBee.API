package com.warrantybee.api.dto.response;

import com.warrantybee.api.enumerations.TaxVerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * Represents the tax registration details of a business for a specific country.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BusinessTaxDetailsResponse {

    /** Unique identifier of the business tax record. */
    private String identifier;

    /** Country for which the tax registration details apply. */
    private CountryResponse country;

    /** Current tax registration status of the business. */
    private TaxVerificationStatus status;

    /** Timestamp indicating when the tax registration was last verified. */
    private Timestamp verifiedAt;
}
