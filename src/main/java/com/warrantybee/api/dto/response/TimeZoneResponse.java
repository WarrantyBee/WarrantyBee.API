package com.warrantybee.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) containing detailed information about a geographical timezone.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeZoneResponse {

    /**
     * Unique database identifier for the timezone entry.
     */
    private Long id;

    /**
     * The full IANA timezone name (e.g., "America/Los_Angeles").
     */
    private String name;

    /**
     * The common abbreviation for the timezone (e.g., "PST" or "PDT").
     */
    private String abbreviation;

    /**
     * The standard (non-DST) offset from UTC, measured in minutes.
     */
    private Short offsetMinutes;

    /**
     * Flag indicating if Daylight Saving Time (DST) is currently observed in this zone.
     */
    private Boolean dst;

    /**
     * The effective offset from UTC in minutes, which includes DST if applicable.
     */
    private Short currentOffsetMinutes;
}
