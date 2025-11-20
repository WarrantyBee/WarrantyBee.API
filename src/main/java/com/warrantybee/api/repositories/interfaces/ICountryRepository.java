package com.warrantybee.api.repositories.interfaces;

import com.warrantybee.api.dto.response.CountryDetailResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for operations related to countries.
 */
@Repository
public interface ICountryRepository {

    /**
     * Retrieves a list of detailed info for countries.
     *
     * @return a list of {@link CountryDetailResponse} objects
     */
    List<CountryDetailResponse> get();
}
