package com.warrantybee.api.enumerations;

import com.warrantybee.api.enumerations.interfaces.IEnumeration;
import lombok.Getter;

/**
 * Defines the security roles available in the WarrantyBee platform.
 * Each role is represented by a unique code and name.
 */
@Getter
public enum SecurityRole implements IEnumeration {

    /** Represents an unspecified or unassigned role. */
    NONE(0, "NONE"),

    /** Full administrative privileges across the platform. */
    SUPER_ADMIN(1, "SUPER_ADMIN"),

    /** Manages product manufacturing details and warranty policy definitions. */
    MANUFACTURER(2, "MANUFACTURER"),

    /** Handles product distribution and vendor-level inventory. */
    VENDOR(3, "VENDOR"),

    /** Manages product sales and retail inventory. */
    RETAILER(4, "RETAILER"),

    /** Oversees service center operations and workflow management. */
    SERVICE_CENTER_MANAGER(5, "SERVICE_CENTER_MANAGER"),

    /** Performs diagnostics, repairs, and product servicing. */
    TECHNICIAN(6, "TECHNICIAN"),

    /** End user who registers products and claims warranties. */
    CUSTOMER(7, "CUSTOMER"),

    /** Handles support tickets and customer interactions. */
    SUPPORT_AGENT(8, "SUPPORT_AGENT"),

    /** Performs system audits, reviews logs, and ensures compliance. */
    AUDITOR(9, "AUDITOR");

    /** Unique integer code associated with the role. */
    private final int code;

    /** Unique string name identifying the role. */
    private final String name;

    /**
     * Creates a new {@code SecurityRole}.
     *
     * @param code the unique integer code of the role
     * @param name the unique string name of the role
     */
    SecurityRole(int code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * Returns the {@link SecurityRole} matching the given role name.
     *
     * @param value the role name (case-insensitive)
     * @return the matching role, or {@code NONE} if invalid
     */
    public static SecurityRole getValue(String value) {
        try {
            return IEnumeration.valueOf(SecurityRole.class, value);
        } catch (IllegalArgumentException e) {
            return SecurityRole.NONE;
        }
    }

    /**
     * Returns the {@link SecurityRole} matching the given integer code.
     *
     * @param value the role code
     * @return the matching role, or {@code NONE} if invalid
     */
    public static SecurityRole getValue(int value) {
        try {
            return IEnumeration.valueOf(SecurityRole.class, value);
        } catch (IllegalArgumentException e) {
            return SecurityRole.NONE;
        }
    }
}
