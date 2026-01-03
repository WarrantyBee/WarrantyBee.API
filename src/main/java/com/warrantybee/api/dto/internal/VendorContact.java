package com.warrantybee.api.dto.internal;

import com.warrantybee.api.enumerations.VendorContactType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a vendor contact detail along with its communication preferences.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VendorContact {

    /** The type of vendor contact (e.g., support, sales, billing). */
    private VendorContactType type;

    /** The contact email address. */
    private String email;

    /** The international dialing code for the phone number. */
    private String phoneCode;

    /** The phone number without the country code. */
    private String phoneNumber;

    /** The country associated with this contact. */
    private Long countryId;

    /** The preferred culture or language for communication. */
    private Long cultureId;

    /** The business hours during which this contact is available (JSON format). */
    private String businessHours;
}
