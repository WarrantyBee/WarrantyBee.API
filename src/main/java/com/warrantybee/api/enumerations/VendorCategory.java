package com.warrantybee.api.enumerations;

import com.warrantybee.api.enumerations.interfaces.IEnumeration;
import lombok.Getter;

/**
 * Represents different categories of vendors supported by the system.
 */
@Getter
public enum VendorCategory implements IEnumeration {

    /** Default value used when no valid category is provided. */
    NONE(0, "NONE"),

    /** Vendor that manufactures products. */
    MANUFACTURER(1, "MANUFACTURER"),

    /** Vendor that distributes products from manufacturers. */
    DISTRIBUTOR(2, "DISTRIBUTOR"),

    /** Vendor that sells products directly to customers. */
    RETAILER(3, "RETAILER"),

    /** Authorized service or repair center for products. */
    SERVICE_CENTER(4, "SERVICE_CENTER"),

    /** Brand owner associated with the product. */
    BRAND(5, "BRAND");

    /** Numeric identifier of the vendor category. */
    private final int code;

    /** Human-readable name of the vendor category. */
    private final String name;

    VendorCategory(int code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * Resolves a {@link VendorCategory} from its string value.
     *
     * @param value the string representation of the vendor category
     * @return the matching {@link VendorCategory}, or {@link #NONE} if invalid
     */
    public static VendorCategory getValue(String value) {
        try {
            return IEnumeration.valueOf(VendorCategory.class, value);
        }
        catch (IllegalArgumentException e) {
            return VendorCategory.NONE;
        }
    }

    /**
     * Resolves a {@link VendorCategory} from its numeric code.
     *
     * @param value the numeric code of the vendor category
     * @return the matching {@link VendorCategory}, or {@link #NONE} if invalid
     */
    public static VendorCategory getValue(int value) {
        try {
            return IEnumeration.valueOf(VendorCategory.class, value);
        }
        catch (IllegalArgumentException e) {
            return VendorCategory.NONE;
        }
    }
}
