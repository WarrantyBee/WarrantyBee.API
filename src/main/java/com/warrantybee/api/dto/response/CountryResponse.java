package com.warrantybee.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents country details returned in API responses.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CountryResponse {

    /** unique identifier of the country. */
    private Long id;

    /** Common name of the country. */
    private String name;

    /** Two-letter ISO 3166-1 alpha-2 country code. */
    private String iso2;

    /** Three-letter ISO 3166-1 alpha-3 country code. */
    private String iso3;

    /** Numeric or internal country code. */
    private String code;

    /** Official full name of the country. */
    private String officialName;

    /** Capital city of the country. */
    private String capital;

    /** International phone dialing code (e.g., +91, +1). */
    private String phoneCode;
}
