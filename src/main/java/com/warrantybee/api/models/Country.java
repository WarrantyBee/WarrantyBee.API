package com.warrantybee.api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a country.
 */
@Entity
@Table(name = "tblCountries")
@Getter
@Setter
public class Country extends BaseEntity<Country> {

    /** ISO 2-letter code */
    @Column(name = "iso2_code", nullable = false, length = 2, insertable = false, updatable = false)
    private String iso2Code;

    /** ISO 3-letter code */
    @Column(name = "iso3_code", nullable = false, length = 3, insertable = false, updatable = false)
    private String iso3Code;

    /** Numeric country code */
    @Column(name = "numeric_code", nullable = false, length = 3, insertable = false, updatable = false)
    private String numericCode;

    /** Country name */
    @Column(name = "name", nullable = false, length = 100, insertable = false, updatable = false)
    private String name;

    /** Official country name */
    @Column(name = "official_name", length = 150, insertable = false, updatable = false)
    private String officialName;

    /** Capital city */
    @Column(name = "capital", length = 100, insertable = false, updatable = false)
    private String capital;

    /** Phone code */
    @Column(name = "phone_code", length = 50, insertable = false, updatable = false)
    private String phoneCode;

    /** Associated currency */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id", insertable = false, updatable = false)
    private Currency currency;
}
