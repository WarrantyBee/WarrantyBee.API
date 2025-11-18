package com.warrantybee.api.services.implementations;

import com.warrantybee.api.dto.response.CountryDetailResponse;
import com.warrantybee.api.repositories.interfaces.ICountryRepository;
import com.warrantybee.api.services.interfaces.ICountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {@link ICountryService} for retrieving detailed country information.
 * Delegates data retrieval to the {@link ICountryRepository}.
 */
@Service
public class CountryService implements ICountryService {

    /** Repository used for fetching country data. */
    private final ICountryRepository _repository;

    /**
     * Constructs a {@code CountryService} with the given repository.
     *
     * @param repository the {@link ICountryRepository} to delegate data access
     */
    @Autowired
    public CountryService(ICountryRepository repository) {
        this._repository = repository;
    }

    /**
     * Retrieves a list of detailed country responses.
     *
     * @return a list of {@link CountryDetailResponse} objects
     */
    @Override
    public List<CountryDetailResponse> get() {
        return _repository.get();
    }
}
