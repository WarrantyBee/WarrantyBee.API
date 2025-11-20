package com.warrantybee.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Represents a detailed response for a country, extending {@link CountryResponse}.
 * Includes additional information such as currency, regions, cultures, and timezones.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CountryDetailResponse extends CountryResponse {

    /** The currency used in the country. */
    private CurrencyResponse currency;

    /** List of regions belonging to the country. */
    private List<RegionResponse> regions;

    /** List of cultures associated with the country. */
    private List<CultureResponse> cultures;

    /** List of timezones applicable to the country. */
    private List<TimeZoneResponse> timezones;
}
