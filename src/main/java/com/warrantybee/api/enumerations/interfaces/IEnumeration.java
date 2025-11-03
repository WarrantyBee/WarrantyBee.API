package com.warrantybee.api.enumerations.interfaces;

/**
 * Defines a standard contract for all enumerations used within the application.
 * This ensures that every enumeration type can provide a unique integer code,
 * facilitating database storage or external system communication.
 */
public interface IEnumeration {

    /**
     * Retrieves the unique integer code associated with this enumeration constant.
     *
     * @return The integer code representing the enumeration value.
     */
    int getCode();
}
