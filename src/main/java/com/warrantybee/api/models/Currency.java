package com.warrantybee.api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tblCurrencies")
@Getter
@Setter
public class Currency extends BaseEntity<Currency> {
    @Column(name = "iso_code", updatable = false, insertable = false, nullable = false, length = 3)
    private String isoCode;

    @Column(name = "numeric_code", updatable = false, insertable = false, length = 3)
    private String numericCode;

    @Column(name = "name", updatable = false, insertable = false, nullable = false, length = 100)
    private String name;

    @Column(name = "symbol", updatable = false, insertable = false, nullable = false, length = 10)
    private String symbol;

    @Column(name = "minor_unit", updatable = false, insertable = false, nullable = false)
    private Short minorUnit;
}
