package com.warrantybee.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a regional or state-level entity within a country.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegionResponse {

    /** Unique identifier of the region. */
    private Long id;

    /** Common name of the region or state. */
    private String name;

    /** Official name of the region. */
    private String official;

    /** ISO or administrative code of the region. */
    private String iso;

    /** Capital city of the region. */
    private String captial;
}
