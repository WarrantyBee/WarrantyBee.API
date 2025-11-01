package com.warrantybee.api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a state or province within a country.
 */
@Entity
@Table(name = "tblStates")
@Getter
@Setter
public class Region extends BaseEntity<Region> {

    /** Name of the state */
    @Column(name = "name", nullable = false, length = 255, insertable = false, updatable = false)
    private String name;

    /** Official name of the state */
    @Column(name = "official_name", length = 150, insertable = false, updatable = false)
    private String officialName;

    /** ISO code of the state */
    @Column(name = "iso_code", nullable = false, length = 10, insertable = false, updatable = false)
    private String isoCode;

    /** Capital city of the state */
    @Column(name = "capital", length = 100, insertable = false, updatable = false)
    private String capital;

    /** Phone code for the state */
    @Column(name = "phone_code", length = 10, insertable = false, updatable = false)
    private String phoneCode;

    /** Associated time zone */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timezone_id", nullable = false, insertable = false, updatable = false)
    private TimeZone timeZone;

    /** Country to which the state belongs */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false, insertable = false, updatable = false)
    private Country country;
}
