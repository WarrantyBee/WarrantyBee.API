package com.warrantybee.api.helpers;

import com.warrantybee.api.enumerations.interfaces.IEnumeration;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
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
    private static final List<String> VALID_PHONE_CODES = List.of(
        "+93", "+355", "+213", "+376", "+244", "+1-268", "+54", "+374", "+61", "+43",
        "+994", "+1-242", "+973", "+880", "+1-246", "+375", "+32", "+501", "+229", "+975",
        "+591", "+387", "+267", "+55", "+673", "+359", "+226", "+257", "+855", "+237",
        "+1", "+238", "+236", "+235", "+56", "+86", "+57", "+269", "+242", "+506",
        "+225", "+385", "+53", "+357", "+420", "+45", "+253", "+1-767", "+1-809",
        "+1-829", "+1-849", "+593", "+20", "+503", "+240", "+291", "+372", "+268",
        "+251", "+679", "+358", "+33", "+241", "+220", "+995", "+49", "+233", "+30",
        "+1-473", "+502", "+224", "+245", "+592", "+509", "+504", "+36", "+354", "+91",
        "+62", "+98", "+964", "+353", "+972", "+39", "+1-876", "+81", "+962", "+7",
        "+254", "+686", "+965", "+996", "+856", "+371", "+961", "+266", "+231", "+218",
        "+423", "+370", "+352", "+261", "+265", "+60", "+960", "+223", "+356", "+692",
        "+222", "+230", "+52", "+691", "+373", "+377", "+976", "+382", "+212", "+258",
        "+95", "+264", "+674", "+977", "+31", "+64", "+505", "+227", "+234", "+850",
        "+389", "+47", "+968", "+92", "+680", "+507", "+675", "+595", "+51", "+63",
        "+48", "+351", "+974", "+40", "+250", "+1-869", "+1-758", "+1-784", "+685",
        "+378", "+239", "+966", "+221", "+381", "+248", "+232", "+65", "+421", "+386",
        "+677", "+252", "+27", "+82", "+211", "+34", "+94", "+249", "+597", "+46",
        "+41", "+963", "+886", "+992", "+255", "+66", "+228", "+676", "+1-868", "+216",
        "+90", "+993", "+688", "+256", "+380", "+971", "+44", "+598", "+998", "+678",
        "+379", "+58", "+84", "+967", "+260", "+263"
    );
    private static final String URL_REGEX = "^(https?://)" + "([\\w.-]+)" + "(:\\d+)?" + "(/[\\w./?%&=-]*)?$";
    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX, Pattern.CASE_INSENSITIVE);
    private static final String PHONE_NUMBER_REGEX = "^[0-9]{10}$";
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile(PHONE_NUMBER_REGEX);
    private static final String POSTAL_CODE_REGEX = "^[0-9]{6}$";
    private static final Pattern POSTAL_CODE_PATTERN = Pattern.compile(POSTAL_CODE_REGEX);
    private static final List<String> ALLOWED_IMAGE_FORMATS = List.of("jpg", "jpeg", "png", "gif", "bmp", "webp", "tiff");

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
     * Checks whether the given file is null or contains no data.
     *
     * @param file the multipart file to check
     * @return {@code true} if the file is null or empty; otherwise {@code false}
     */
    public static boolean isEmpty(MultipartFile file) {
        return file == null || file.isEmpty();
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

    /**
     * Checks if the given String value is a valid country phone code.
     * <p>
     * The validation first ensures the string is not blank. It then checks if the trimmed string
     * exists in the predefined, static list of {@code VALID_PHONE_CODES}.
     *
     * @param value The String to be checked (e.g., "+1", "+44", "+91").
     * @return {@code true} if the string is a recognized phone code; {@code false} if it is blank
     * or not found in the list of valid codes.
     */
    public static boolean isPhoneCode(String value) {
        if (isBlank(value)) {
            return false;
        }

        return VALID_PHONE_CODES.contains(value.trim());
    }

    /**
     * Determines whether the given {@link MultipartFile} represents a valid image file.
     *
     * @param file the multipart file to validate; may be null
     * @return {@code true} if the file is a non-empty image with a valid MIME type
     *         and a recognized image extension; {@code false} otherwise.
     */
    public static boolean isImage(MultipartFile file) {
        if (isEmpty(file)) {
            return false;
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return false;
        }

        String name = file.getOriginalFilename();
        if (name == null || !name.contains(".")) {
            return false;
        }

        String extension = name.substring(name.lastIndexOf('.') + 1).toLowerCase();
        return ALLOWED_IMAGE_FORMATS.contains(extension);
    }

    /**
     * Checks whether the file is not empty and its size is within the given limit.
     *
     * @param file the multipart file to check
     * @param maxSizeInBytes the maximum allowed file size in bytes
     * @return {@code true} if the file exists and its size is within limit; otherwise {@code false}
     */
    public static boolean hasSize(MultipartFile file, long maxSizeInBytes) {
        if (isEmpty(file)) {
            return false;
        }

        return file.getSize() <= maxSizeInBytes;
    }

    /**
     * Checks whether the given string is a valid HTTP or HTTPS URL.
     *
     * @param value the string to validate
     * @return {@code true} if the value is a well-formed HTTP/HTTPS URL; {@code false} otherwise
     */
    public static boolean isUrl(String value) {
        return !isBlank(value) && URL_PATTERN.matcher(value.trim()).matches();
    }

    /**
     * Checks whether the given string is a valid 10-digit phone number.
     *
     * @param value the phone number string to validate
     * @return {@code true} if the value is a valid 10-digit number; {@code false} otherwise
     */
    public static boolean isPhoneNumber(String value) {
        return !isBlank(value) && PHONE_NUMBER_PATTERN.matcher(value.trim()).matches();
    }

    /**
     * Checks whether the given string is a valid 6-digit postal code.
     *
     * @param value the postal code string to validate
     * @return {@code true} if the value is a valid 6-digit postal code; {@code false} otherwise
     */
    public static boolean isPostalCode(String value) {
        return !isBlank(value) && POSTAL_CODE_PATTERN.matcher(value.trim()).matches();
    }
}
