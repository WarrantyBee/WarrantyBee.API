package com.warrantybee.api.dto.response;

import com.warrantybee.api.enumerations.VendorCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Represents the public-facing business profile details of a vendor or organization.
 * This information is typically exposed to customers, partners, or external systems.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BusinessProfileResponse {

    /** Trade or display name under which the business operates. */
    private String name;

    /** Official registered legal name of the business entity. */
    private String legalName;

    /** Unique internal or external identifier assigned to the business. */
    private String code;

    /** Business categories defining the role or operational nature of the vendor. */
    private List<VendorCategory> categories;

    /** Primary email address used for official business correspondence. */
    private String primaryEmail;

    /** International dialing code associated with the primary phone number. */
    private String primaryPhoneCode;

    /** Primary contact phone number for business communication. */
    private String primaryPhoneNumber;

    /** Official website URL representing the business online presence. */
    private String website;

    /** Dedicated support or helpdesk email address for customer or partner queries. */
    private String supportEmail;
}
