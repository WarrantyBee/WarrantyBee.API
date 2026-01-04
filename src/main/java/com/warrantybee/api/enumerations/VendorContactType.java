package com.warrantybee.api.enumerations;

import com.warrantybee.api.enumerations.interfaces.IEnumeration;
import lombok.Getter;

/**
 * Represents different types of vendor contacts based on their role or purpose within the system.
 */
@Getter
public enum VendorContactType implements IEnumeration {

    /** Default value used when no valid contact type is provided. */
    NONE(0, "NONE"),

    /** Primary point of contact for the vendor. */
    PRIMARY(1, "PRIMARY"),

    /** General business communication contact. */
    BUSINESS(2, "BUSINESS"),

    /** Contact responsible for sales and commercial inquiries. */
    SALES(3, "SALES"),

    /** Contact responsible for billing, invoices, and payments. */
    ACCOUNTING(4, "ACCOUNTING"),

    /** Contact for customer or technical support queries. */
    SUPPORT(5, "SUPPORT"),

    /** Contact handling day-to-day operational matters. */
    OPERATIONS(6, "OPERATIONS"),

    /** Contact responsible for service and repair coordination. */
    SERVICE(7, "SERVICE"),

    /** Contact for legal matters such as contracts and agreements. */
    LEGAL(8, "LEGAL"),

    /** Contact responsible for regulatory and compliance-related matters. */
    COMPLIANCE(9, "COMPLIANCE"),

    /** Contact responsible for data protection and privacy concerns. */
    DATA_PROTECTION(10, "DATA_PROTECTION"),

    /** Contact for handling security incidents and vulnerabilities. */
    SECURITY(11, "SECURITY"),

    /** Escalation contact for critical or high-priority issues. */
    ESCALATION(12, "ESCALATION"),

    /** Technical contact for engineering or system integrations. */
    TECHNICAL(13, "TECHNICAL"),

    /** Contact responsible for API and webhook communications. */
    API(14, "API"),

    /** Contact responsible for logistics and supply chain coordination. */
    LOGISTICS(15, "LOGISTICS"),

    /** Contact responsible for procurement and sourcing activities. */
    PROCUREMENT(16, "PROCUREMENT"),

    /** Contact responsible for region-specific coordination. */
    REGIONAL(17, "REGIONAL"),

    /** Contact responsible for local or branch-level coordination. */
    LOCAL(18, "LOCAL"),

    /** Custom contact type defined based on vendor-specific or business-specific requirements. */
    CUSTOM(19, "CUSTOM");

    /** Numeric identifier of the vendor contact type. */
    private final int code;

    /** Human-readable name of the vendor contact type. */
    private final String name;

    VendorContactType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * Resolves a {@link VendorContactType} from its numeric code.
     *
     * @param value the numeric code of the vendor contact type
     * @return the matching {@link VendorContactType}, or {@link #NONE} if invalid
     */
    public static VendorContactType getValue(int value) {
        try {
            return IEnumeration.valueOf(VendorContactType.class, value);
        } catch (IllegalArgumentException e) {
            return NONE;
        }
    }

    /**
     * Resolves a {@link VendorContactType} from its string value.
     *
     * @param value the string representation of the vendor contact type
     * @return the matching {@link VendorContactType}, or {@link #NONE} if invalid
     */
    public static VendorContactType getValue(String value) {
        try {
            return IEnumeration.valueOf(VendorContactType.class, value);
        } catch (IllegalArgumentException e) {
            return NONE;
        }
    }
}
