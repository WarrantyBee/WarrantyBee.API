package com.warrantybee.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) representing detailed country information
 * for API responses.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CountryResponse {

    /** The unique database identifier of the country. */
    private Long id;

    /** The common, standard name of the country (e.g., "United States"). */
    private String name;

    /** The two-letter ISO 3166-1 alpha-2 country code (e.g., "US"). */
    private String iso2;

    /** The three-letter ISO 3166-1 alpha-3 country code (e.g., "USA"). */
    private String iso3;

    /** A numeric or alternative internal country code, if applicable. */
    private String code;

    /** The official full name of the country (e.g., "United States of America"). */
    private String officialName;

    /** The capital city of the country. */
    private String capital;

    /** The international phone dialing code (e.g., "+91", "+1"). */
    private String phoneCode;
}
