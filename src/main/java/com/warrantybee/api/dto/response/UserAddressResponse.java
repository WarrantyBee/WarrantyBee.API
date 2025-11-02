package com.warrantybee.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for conveying a user's complete mailing and geographical address.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAddressResponse {

    /**
     * The first line of the street address.
     */
    private String addressLine1;

    /**
     * The second line of the street address (optional, e.g., apartment or suite number).
     */
    private String addressLine2;

    /**
     * The city or municipality name.
     */
    private String city;

    /**
     * The postal code, ZIP code, or equivalent area code.
     */
    private String postalCode;

    /**
     * The region or state information, provided as a nested {@link RegionResponse} DTO.
     */
    private RegionResponse region;

    /**
     * The country information, provided as a nested {@link CountryResponse} DTO.
     */
    private CountryResponse country;
}
