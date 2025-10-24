package com.warrantybee.api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a currency.
 */
@Entity
@Table(name = "tblCurrencies")
@Getter
@Setter
public class Currency extends BaseEntity<Currency> {

    /** ISO currency code */
    @Column(name = "iso_code", updatable = false, insertable = false, nullable = false, length = 3)
    private String isoCode;

    /** Numeric currency code */
    @Column(name = "numeric_code", updatable = false, insertable = false, length = 3)
    private String numericCode;

    /** Currency name */
    @Column(name = "name", updatable = false, insertable = false, nullable = false, length = 100)
    private String name;

    /** Currency symbol */
    @Column(name = "symbol", updatable = false, insertable = false, nullable = false, length = 10)
    private String symbol;

    /** Minor unit (e.g., cents) */
    @Column(name = "minor_unit", updatable = false, insertable = false, nullable = false)
    private Short minorUnit;
}
