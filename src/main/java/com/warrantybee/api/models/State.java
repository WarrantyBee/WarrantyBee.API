package com.warrantybee.api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tblStates")
@Getter
@Setter
public class State extends BaseEntity<State> {

    @Column(name = "name", nullable = false, length = 255, insertable = false, updatable = false)
    private String name;

    @Column(name = "official_name", length = 150, insertable = false, updatable = false)
    private String officialName;

    @Column(name = "iso_code", nullable = false, length = 10, insertable = false, updatable = false)
    private String isoCode;

    @Column(name = "capital", length = 100, insertable = false, updatable = false)
    private String capital;

    @Column(name = "phone_code", length = 10, insertable = false, updatable = false)
    private String phoneCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timezone_id", nullable = false, insertable = false, updatable = false)
    private TimeZone timeZone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false, insertable = false, updatable = false)
    private Country country;
}
