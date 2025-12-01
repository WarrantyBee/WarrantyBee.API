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

    /**
     * Retrieves an enum constant of the specified enum type by its name.
     * Matching is case-insensitive.
     *
     * @param enumType the class type of the enum
     * @param name     the name of the enum constant to find
     * @param <T>      the enum type
     * @return the matching enum constant
     * @throws IllegalArgumentException if no matching enum constant is found
     */
    static <T extends Enum<T> & IEnumeration> T valueOf(Class<T> enumType, String name) {
        if (name == null) {
            throw new IllegalArgumentException("Enum name cannot be null.");
        }

        for (T constant : enumType.getEnumConstants()) {
            if (constant.name().equalsIgnoreCase(name.trim())) {
                return constant;
            }
        }

        throw new IllegalArgumentException("No enum constant " + enumType.getSimpleName()
                + " with name: " + name);
    }

    /**
     * Retrieves an enum constant of the specified enum type by its integer code.
     *
     * @param enumType the class type of the enum
     * @param code     the integer code of the enum constant
     * @param <T>      the enum type
     * @return the matching enum constant
     * @throws IllegalArgumentException if no matching enum constant is found
     */
    static <T extends Enum<T> & IEnumeration> T valueOf(Class<T> enumType, int code) {
        for (T constant : enumType.getEnumConstants()) {
            if (constant.getCode() == code) {
                return constant;
            }
        }

        throw new IllegalArgumentException(
                "No enum constant " + enumType.getSimpleName() + " with code: " + code
        );
    }
}
