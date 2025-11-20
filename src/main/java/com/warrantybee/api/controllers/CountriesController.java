package com.warrantybee.api.controllers;

import com.warrantybee.api.dto.response.APIResponse;
import com.warrantybee.api.dto.response.CountryDetailResponse;
import com.warrantybee.api.services.interfaces.ICountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for managing country-related endpoints.
 * Provides APIs to retrieve detailed country information.
 */
@RestController
@RequestMapping("/api/countries")
public class CountriesController {

    /** Service used to fetch country details. */
    private final ICountryService _service;

    /**
     * Constructs the {@code CountriesController} with the specified country service.
     *
     * @param service the {@link ICountryService} to fetch country data
     */
    @Autowired
    public CountriesController(ICountryService service) {
        this._service = service;
    }

    /**
     * GET endpoint to retrieve a list of detailed country responses.
     *
     * @return a {@link ResponseEntity} containing an {@link APIResponse}
     *         with a list of {@link CountryDetailResponse} objects
     */
    @GetMapping
    public ResponseEntity<APIResponse<List<CountryDetailResponse>>> get() {
        final APIResponse<List<CountryDetailResponse>> response = new APIResponse<>(_service.get());
        return ResponseEntity.ok(response);
    }
}
