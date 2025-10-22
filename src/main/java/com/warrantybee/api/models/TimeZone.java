package com.warrantybee.api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tblTimeZones")
@Getter
@Setter
public class TimeZone extends BaseEntity<TimeZone> {
    @Column(name = "name", nullable = false, length = 100, insertable = false, updatable = false)
    private String name;

    @Column(name = "abbreviation", length = 10, insertable = false, updatable = false)
    private String abbreviation;

    @Column(name = "utc_offset_minutes", nullable = false, insertable = false, updatable = false)
    private Short utcOffsetMinutes;

    @Column(name = "observes_dst", nullable = false, insertable = false, updatable = false)
    private Boolean observesDst;

    @Column(name = "current_offset_minutes", nullable = false, insertable = false, updatable = false)
    private Short currentOffsetMinutes;
}
