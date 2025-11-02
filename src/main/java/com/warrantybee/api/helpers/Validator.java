package com.warrantybee.api.helpers;

import com.warrantybee.api.enumerations.interfaces.IEnumeration;

import java.time.LocalDate;
import java.util.regex.Pattern;

/**
 * Utility class providing static helper methods for common validation tasks.
 * This class primarily focuses on checking the nullity and content of String values.
 */
public class Validator {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    private static final String STRONG_PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()]).{8,}$";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final Pattern STRONG_PASSWORD_PATTERN = Pattern.compile(STRONG_PASSWORD_REGEX);

    /**
     * Checks if a given String is null, empty ({@code ""}), or contains only whitespace characters.
     * This is a robust check often used for input validation where a string field is mandatory.
     *
     * @param value The String to be checked.
     * @return {@code true} if the string is {@code null}, empty, or consists
     * only of whitespace characters (e.g., space, tab, newline); {@code false} otherwise.
     */
    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * Checks if the given String is a valid email address format.
     * * The validation first ensures the string is not blank, and then checks
     * its format against a standard regular expression pattern.
     *
     * @param value The String to be checked for email format validity.
     * @return {@code true} if the string is a valid email format, {@code false} otherwise.
     */
    public static boolean isEmail(String value) {
        return !isBlank(value) && EMAIL_PATTERN.matcher(value).matches();
    }

    /**
     * Checks if the given String meets the criteria for a strong password.
     * The criteria enforced by the current pattern are:
     * <ul>
     * <li>Minimum 8 characters in length.</li>
     * <li>Contains at least one uppercase letter (A-Z).</li>
     * <li>Contains at least one lowercase letter (a-z).</li>
     * <li>Contains at least one digit (0-9).</li>
     * <li>Contains at least one special character from the set: !@#$%^&*().</li>
     * </ul>
     * The password must also not be blank.
     *
     * @param value The String to be checked for password strength.
     * @return {@code true} if the string meets all strong password criteria, {@code false} otherwise.
     */
    public static boolean isStrongPassword(String value) {
        return !isBlank(value) && STRONG_PASSWORD_PATTERN.matcher(value).matches();
    }

    /**
     * Checks if the given Byte value corresponds to a valid code within the specified enum type.
     * <p>
     * This method requires the enum type {@code T} to implement the {@link com.warrantybee.api.enumerations.interfaces.IEnumeration} interface,
     * which exposes the {@code getCode()} method for comparison.
     *
     * @param <T> The enumeration type, constrained to implement {@link com.warrantybee.api.enumerations.interfaces.IEnumeration}.
     * @param value The Byte value (code) to validate.
     * @param enumType The Class object of the target enumeration (e.g., {@code Gender.class}).
     * @return {@code true} if a matching enum constant is found; {@code false} otherwise.
     */
    public static <T extends Enum<T> & IEnumeration> boolean isEnum(Byte value, Class<T> enumType) {
        if (value == null || enumType == null) {
            return false;
        }

        int targetCode = value.intValue();

        for (T enumConstant : enumType.getEnumConstants()) {
            if (enumConstant.getCode() == targetCode) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the given birthdate indicates the person has reached the legal age of 18 or older.
     * The check compares the provided date of birth against the date 18 years prior to the current date.
     *
     * @param value The LocalDate representing the person's date of birth.
     * @return {@code true} if the person is 18 years or older as of today, {@code false} otherwise.
     */
    public static boolean hasLegalAge(LocalDate value) {
        if (value == null) {
            return false;
        }
        LocalDate legalAgeCutoff = LocalDate.now().minusYears(18);
        return !value.isAfter(legalAgeCutoff);
    }
}
