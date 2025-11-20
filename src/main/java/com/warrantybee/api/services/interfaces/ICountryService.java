package com.warrantybee.api.services.interfaces;

import com.warrantybee.api.dto.response.CountryDetailResponse;

import java.util.List;

/**
 * Service interface for retrieving detailed country information.
 */
public interface ICountryService {

    /**
     * Retrieves a list of detailed country responses.
     *
     * @return a list of {@link CountryDetailResponse} objects
     */
    List<CountryDetailResponse> get();
}
