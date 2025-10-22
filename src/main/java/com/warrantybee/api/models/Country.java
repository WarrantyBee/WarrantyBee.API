package com.warrantybee.api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tblCountries")
@Getter
@Setter
public class Country extends BaseEntity<Country> {
    @Column(name = "iso2_code", nullable = false, length = 2, insertable = false, updatable = false)
    private String iso2Code;

    @Column(name = "iso3_code", nullable = false, length = 3, insertable = false, updatable = false)
    private String iso3Code;

    @Column(name = "numeric_code", nullable = false, length = 3, insertable = false, updatable = false)
    private String numericCode;

    @Column(name = "name", nullable = false, length = 100, insertable = false, updatable = false)
    private String name;

    @Column(name = "official_name", length = 150, insertable = false, updatable = false)
    private String officialName;

    @Column(name = "capital", length = 100, insertable = false, updatable = false)
    private String capital;

    @Column(name = "phone_code", length = 50, insertable = false, updatable = false)
    private String phoneCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id", insertable = false, updatable = false)
    private Currency currency;
}
