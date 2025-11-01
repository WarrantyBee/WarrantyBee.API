package com.warrantybee.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents timezone details associated with a region or country.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeZoneResponse {

    /** Unique identifier of the timezone. */
    private Long id;

    /** Full name of the timezone (e.g., Asia/Kolkata, America/New_York). */
    private String name;

    /** Abbreviation of the timezone (e.g., IST, EST, PST). */
    private String abbreviation;

    /** Standard offset from UTC in minutes. */
    private Short offsetMinutes;

    /** Indicates whether daylight saving time (DST) is observed. */
    private Boolean dst;

    /** Current effective offset from UTC in minutes, accounting for DST. */
    private Short currentOffsetMinutes;
}
