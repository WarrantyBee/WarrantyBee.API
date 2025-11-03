package com.warrantybee.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) representing a response structure for a regional or state-level entity.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegionResponse {

    /**
     * Unique database identifier for the region entity.
     */
    private Long id;

    /**
     * The commonly used name of the region or state (e.g., "California", "Bavaria").
     */
    private String name;

    /**
     * The ISO 3166-2 or other administrative code identifying the region (e.g., "US-CA").
     */
    private String iso;

    /**
     * The capital city of the region or state.
     */
    private String capital;
}
