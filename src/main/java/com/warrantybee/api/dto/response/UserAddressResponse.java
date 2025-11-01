package com.warrantybee.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a user's address details, including city, postal code,
 * region, and country information.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAddressResponse {

    /** First line of the address. */
    private String addressLine1;

    /** Second line of the address (optional). */
    private String addressLine2;

    /** Name of the city. */
    private String city;

    /** Postal or ZIP code of the address. */
    private String postalCode;

    /** Region or state information associated with the address. */
    private RegionResponse region;

    /** Country information associated with the address. */
    private CountryResponse country;
}
