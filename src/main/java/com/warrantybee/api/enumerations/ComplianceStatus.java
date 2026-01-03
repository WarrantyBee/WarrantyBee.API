package com.warrantybee.api.enumerations;

import com.warrantybee.api.enumerations.interfaces.IEnumeration;
import lombok.Getter;

/**
 * Represents the compliance lifecycle status of a vendor.
 * This indicates the current state of legal, tax, and regulatory verification.
 */
@Getter
public enum ComplianceStatus implements IEnumeration {

    /** Default value used when no valid compliance status is provided. */
    NONE(0, "NONE"),

    /** Vendor has been created but no compliance data has been submitted yet. */
    PENDING(1, "PENDING"),

    /** Vendor has submitted required compliance documents. */
    SUBMITTED(2, "SUBMITTED"),

    /** Compliance documents are currently under review. */
    UNDER_REVIEW(3, "UNDER_REVIEW"),

    /** Vendor is partially verified; some documents are still pending. */
    PARTIALLY_VERIFIED(4, "PARTIALLY_VERIFIED"),

    /** Vendor has been fully verified and is compliant. */
    VERIFIED(5, "VERIFIED"),

    /** Submitted compliance documents were rejected. */
    REJECTED(6, "REJECTED"),

    /** Vendor’s compliance has expired and requires renewal. */
    EXPIRED(7, "EXPIRED"),

    /** Vendor’s compliance has been suspended due to violations or risk. */
    SUSPENDED(8, "SUSPENDED"),

    /** Compliance requirement has been explicitly waived by an administrator. */
    WAIVED(9, "WAIVED");

    /** Numeric identifier of the compliance status. */
    private final int code;

    /** Human-readable name of the compliance status. */
    private final String name;

    ComplianceStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * Resolves a {@link ComplianceStatus} from its string value.
     *
     * @param value the string representation of the compliance status
     * @return the matching {@link ComplianceStatus}, or {@link #NONE} if invalid
     */
    public static ComplianceStatus getValue(String value) {
        try {
            return IEnumeration.valueOf(ComplianceStatus.class, value);
        }
        catch (IllegalArgumentException e) {
            return ComplianceStatus.NONE;
        }
    }

    /**
     * Resolves a {@link ComplianceStatus} from its numeric code.
     *
     * @param value the numeric code of the compliance status
     * @return the matching {@link ComplianceStatus}, or {@link #NONE} if invalid
     */
    public static ComplianceStatus getValue(int value) {
        try {
            return IEnumeration.valueOf(ComplianceStatus.class, value);
        }
        catch (IllegalArgumentException e) {
            return ComplianceStatus.NONE;
        }
    }
}
