package com.warrantybee.api.enumerations;

import com.warrantybee.api.enumerations.interfaces.IEnumeration;
import lombok.Getter;

/**
 * Represents the lifecycle status of a vendor's tax registration.
 * This determines whether the vendor is legally allowed to issue
 * tax-compliant invoices and participate in financial transactions.
 */
@Getter
public enum TaxVerificationStatus implements IEnumeration {

    /** Default value used when no valid tax registration status is provided. */
    NONE(0, "NONE"),

    /** Vendor has applied for tax registration, but it is not yet approved. */
    PENDING(1, "PENDING"),

    /** Vendor's tax registration is active and fully valid. */
    ACTIVE(2, "ACTIVE"),

    /** Vendor's tax registration has been temporarily suspended. */
    SUSPENDED(3, "SUSPENDED"),

    /** Vendor's tax registration has been permanently cancelled. */
    CANCELLED(4, "CANCELLED"),

    /** Vendor's tax registration has expired and requires renewal. */
    EXPIRED(5, "EXPIRED");

    /** Numeric identifier of the tax registration status. */
    private final int code;

    /** Human-readable name of the tax registration status. */
    private final String name;

    TaxVerificationStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * Resolves a {@link TaxVerificationStatus} from its string value.
     *
     * @param value the string representation of the tax registration status
     * @return the matching {@link TaxVerificationStatus}, or {@link #NONE} if invalid
     */
    public static TaxVerificationStatus getValue(String value) {
        try {
            return IEnumeration.valueOf(TaxVerificationStatus.class, value);
        }
        catch (IllegalArgumentException e) {
            return TaxVerificationStatus.NONE;
        }
    }

    /**
     * Resolves a {@link TaxVerificationStatus} from its numeric code.
     *
     * @param value the numeric code of the tax registration status
     * @return the matching {@link TaxVerificationStatus}, or {@link #NONE} if invalid
     */
    public static TaxVerificationStatus getValue(int value) {
        try {
            return IEnumeration.valueOf(TaxVerificationStatus.class, value);
        }
        catch (IllegalArgumentException e) {
            return TaxVerificationStatus.NONE;
        }
    }
}
