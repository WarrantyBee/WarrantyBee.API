package com.warrantybee.api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a time zone.
 */
@Entity
@Table(name = "tblTimeZones")
@Getter
@Setter
public class TimeZone extends BaseEntity<TimeZone> {

    /** Time zone name */
    @Column(name = "name", nullable = false, length = 100, insertable = false, updatable = false)
    private String name;

    /** Abbreviation (e.g., PST, IST) */
    @Column(name = "abbreviation", length = 10, insertable = false, updatable = false)
    private String abbreviation;

    /** Standard UTC offset in minutes */
    @Column(name = "utc_offset_minutes", nullable = false, insertable = false, updatable = false)
    private Short utcOffsetMinutes;

    /** Whether the time zone observes daylight saving */
    @Column(name = "observes_dst", nullable = false, insertable = false, updatable = false)
    private Boolean observesDst;

    /** Current UTC offset in minutes */
    @Column(name = "current_offset_minutes", nullable = false, insertable = false, updatable = false)
    private Short currentOffsetMinutes;
}
