package com.warrantybee.api.enumerations;

import com.warrantybee.api.enumerations.interfaces.IEnumeration;
import lombok.Getter;

/**
 * Defines the statutory and tax registration types under which a business may be registered.
 */
@Getter
public enum BusinessRegistrationType implements IEnumeration {

    /** Default value when the registration type is unknown or not provided. */
    NONE(0, "NONE"),

    /** Goods and Services Tax registration. */
    GST(1, "GST"),

    /** Value Added Tax registration. */
    VAT(2, "VAT"),

    /** Permanent Account Number issued for tax identification. */
    PAN(3, "PAN"),

    /** Corporate Identification Number for incorporated entities. */
    CIN(4, "CIN"),

    /** Employer Identification Number issued in the United States. */
    EIN(5, "EIN"),

    /** Generic Tax Identification Number used across regions. */
    TIN(6, "TIN"),

    /** Trade license issued by a local authority. */
    TRADE_LICENSE(7, "TRADE_LICENSE"),

    /** Business incorporation or company registration number. */
    INCORPORATION(8, "INCORPORATION"),

    /** Sales tax registration at state or regional level. */
    SALES_TAX(9, "SALES_TAX"),

    /** Legacy service tax registration, where applicable. */
    SERVICE_TAX(10, "SERVICE_TAX");

    /** Numeric identifier of the registration type. */
    private final int code;

    /** Name of the registration type. */
    private final String name;

    BusinessRegistrationType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * Resolves a registration type from its string value.
     */
    public static BusinessRegistrationType getValue(String value) {
        try {
            return IEnumeration.valueOf(BusinessRegistrationType.class, value);
        }
        catch (IllegalArgumentException e) {
            return BusinessRegistrationType.NONE;
        }
    }

    /**
     * Resolves a registration type from its numeric code.
     */
    public static BusinessRegistrationType getValue(int value) {
        try {
            return IEnumeration.valueOf(BusinessRegistrationType.class, value);
        }
        catch (IllegalArgumentException e) {
            return BusinessRegistrationType.NONE;
        }
    }
}
