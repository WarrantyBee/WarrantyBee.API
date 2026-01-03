package com.warrantybee.api.dto.response;

import com.warrantybee.api.enumerations.BusinessRegistrationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents the business registration details of an entity.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BusinessRegistrationResponse {

    /** Official business registration number issued by the authority. */
    private String registrationNumber;

    /** Type of business registration (e.g., INCORPORATION, GST, VAT, TRADE_LICENSE). */
    private BusinessRegistrationType type;
}
