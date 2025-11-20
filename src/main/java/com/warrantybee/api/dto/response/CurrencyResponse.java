package com.warrantybee.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for currency details returned in API responses.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyResponse {

    /** The unique database identifier of the currency. */
    private Long id;

    /** The full name of the currency (e.g., US Dollar, Indian Rupee). */
    private String name;

    /** The ISO 4217 alphabetic code (e.g., USD, INR). */
    private String iso;

    /** A short numeric or alternative internal code representing the currency. */
    private String code;

    /** The symbol used to represent the currency (e.g., $, ₹, €). */
    private String symbol;

    /** The number of decimal places (minor units) supported by the currency (e.g., 2 for USD). */
    private Byte minorUnit;
}
