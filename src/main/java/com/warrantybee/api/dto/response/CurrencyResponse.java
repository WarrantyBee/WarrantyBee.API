package com.warrantybee.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents currency details returned in API responses.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyResponse {

    /** Unique identifier of the currency. */
    private Long id;

    /** Full name of the currency (e.g., US Dollar, Indian Rupee). */
    private String name;

    /** ISO 4217 alphabetic code of the currency (e.g., USD, INR). */
    private String iso;

    /** Short numeric or internal code representing the currency. */
    private String code;

    /** Symbol used to represent the currency (e.g., $, ₹, €). */
    private String symbol;

    /** Number of decimal places (minor units) supported by the currency. */
    private Byte minorUnit;
}
