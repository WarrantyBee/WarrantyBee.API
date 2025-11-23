package com.warrantybee.api.enumerations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Represents standardized error codes used across the API.
 */
@Getter
@AllArgsConstructor
public enum Error {

    /** Invalid or malformed input provided. */
    INVALID_INPUT(1001, "Invalid input provided.", HttpStatus.BAD_REQUEST),

    /** Incorrect login credentials. */
    INVALID_LOGIN_CREDENTIALS(1002, "Invalid email or password.", HttpStatus.BAD_REQUEST),

    /** Missing or invalid authentication token. */
    UNAUTHENTICATED(1003, "Missing or invalid authentication token.", HttpStatus.UNAUTHORIZED),

    /** User lacks permission to access the resource. */
    UNAUTHORIZED_ACCESS(1004, "User does not have permission to access this resource.", HttpStatus.FORBIDDEN),

    /** Requested resource was not found. */
    RESOURCE_NOT_FOUND(1005, "The requested resource was not found.", HttpStatus.NOT_FOUND),

    /** Requested user was not found. */
    USER_NOT_FOUND(1006, "The specified user was not found.", HttpStatus.NOT_FOUND),

    /** Resource already exists in the system. */
    RESOURCE_ALREADY_EXISTS(1007, "This resource already exists.", HttpStatus.CONFLICT),

    /** Email already exists in the system. */
    EMAIL_ALREADY_EXISTS(1008, "An account with this email already exists.", HttpStatus.CONFLICT),

    /** Unexpected server-side error. */
    INTERNAL_SERVER_ERROR(1009, "An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR),

    /** Error occurred while interacting with cache. */
    CACHE_ERROR(1010, "There was an error with the cache service.", HttpStatus.INTERNAL_SERVER_ERROR),

    /** Error occurred while interacting with telemetry. */
    TELEMETRY_SERVICE_ERROR(1011, "There was an error with the telemetry service.", HttpStatus.INTERNAL_SERVER_ERROR),

    /** Error occurred while interacting with captcha. */
    CAPTCHA_SERVICE_ERROR(1012, "There was an error with the captcha service.", HttpStatus.INTERNAL_SERVER_ERROR),

    /** The provided token is invalid. */
    INVALID_TOKEN(1013, "The provided token is invalid.", HttpStatus.UNAUTHORIZED),

    /** Could not generate JWT token. */
    JWT_GENERATION_ERROR(1014, "Could not generate JWT token.", HttpStatus.INTERNAL_SERVER_ERROR),

    /** The provided captcha is invalid. */
    INVALID_CAPTCHA(1015, "Given captcha is expired.", HttpStatus.BAD_REQUEST),

    /** The specified user is not registered. */
    USER_NOT_REGISTERED(1016, "The specified user is not registered.", HttpStatus.NOT_FOUND),

    /** The specified user is already registered. */
    USER_ALREADY_REGISTERED(1017, "The specified user is already registered.", HttpStatus.CONFLICT),

    /** The request body is empty. */
    REQUEST_BODY_EMPTY(1018, "Request body is empty.", HttpStatus.BAD_REQUEST),

    /** The captcha response is required. */
    CAPTCHA_RESPONSE_REQUIRED(1019, "Captcha response is required.", HttpStatus.BAD_REQUEST),

    /** The firstname is required. */
    FIRSTNAME_REQUIRED(1020, "Firstname is required.", HttpStatus.BAD_REQUEST),

    /** The lastname is required. */
    LASTNAME_REQUIRED(1021, "Lastname is required.", HttpStatus.BAD_REQUEST),

    /** The email is required. */
    EMAIL_REQUIRED(1022, "Email is required.", HttpStatus.BAD_REQUEST),

    /** The password is required. */
    PASSWORD_REQUIRED(1023, "Password is required.", HttpStatus.BAD_REQUEST),

    /** The address is required. */
    ADDRESS_REQUIRED(1024, "Address is required.", HttpStatus.BAD_REQUEST),

    /** The city is required. */
    CITY_REQUIRED(1025, "City is required.", HttpStatus.BAD_REQUEST),

    /** The postal code is required. */
    POSTAL_CODE_REQUIRED(1026, "Postal code is required.", HttpStatus.BAD_REQUEST),

    /** The email is invalid. */
    INVALID_EMAIL(1027, "Invalid email format.", HttpStatus.BAD_REQUEST),

    /** The gender value is invalid. */
    INVALID_GENDER_VALUE(1028, "Invalid gender value.", HttpStatus.BAD_REQUEST),

    /** The user is a minor. */
    USER_IS_MINOR(1029, "User must be at least 18 years old.", HttpStatus.BAD_REQUEST),

    /** The password is not strong enough. */
    STRONG_PASSWORD_REQUIRED(1030, "Password is not strong enough.", HttpStatus.BAD_REQUEST),

    /** The phone number is required. */
    PHONE_NUMBER_REQUIRED(1031, "Phone number is required.", HttpStatus.BAD_REQUEST),

    /** The user registration failed. */
    USER_REGISTRATION_FAILED(1032, "User registration failed.", HttpStatus.INTERNAL_SERVER_ERROR),

    /** The otp receiver is required. */
    OTP_RECIPIENT_REQUIRED(1033, "The OTP recipient is required.", HttpStatus.BAD_REQUEST),

    /** The otp is invalid. */
    INVALID_OTP(1034, "Invalid OTP.", HttpStatus.BAD_REQUEST),

    /** The otp has expired. */
    OTP_EXPIRED(1035, "OTP has expired.", HttpStatus.BAD_REQUEST),

    /** The maximum otp attempts reached. */
    MAX_OTP_ATTEMPTS_REACHED(1036, "Maximum OTP attempts reached.", HttpStatus.BAD_REQUEST),

    /** Could not generate OTP. */
    OTP_GENERATION_FAILED(1039, "Could not generate OTP.", HttpStatus.INTERNAL_SERVER_ERROR),

    /** The email template was not found. */
    EMAIL_TEMPLATE_NOT_FOUND(1037, "Email template not found.", HttpStatus.NOT_FOUND),

    /** There was an error parsing the email template. */
    EMAIL_TEMPLATE_PARSING_ERROR(1038, "There was an error parsing the email template.", HttpStatus.INTERNAL_SERVER_ERROR),

    /** Could not resolve all macros in the email template. */
    TEMPLATE_MACRO_COULD_NOT_BE_RESOLVED(1040, "Could not resolve all macros in the email template.", HttpStatus.INTERNAL_SERVER_ERROR),

    /** The rate limit has been exceeded. */
    RATE_LIMIT_EXCEEDED(1041, "Rate limit exceeded. Please try again later.", HttpStatus.TOO_MANY_REQUESTS),

    /** The OTP is required. */
    OTP_REQUIRED(1042, "The OTP is required.", HttpStatus.BAD_REQUEST),

    /** The OTP recipient is invalid. */
    INVALID_OTP_RECIPIENT(1043, "The OTP recipient is invalid.", HttpStatus.BAD_REQUEST),

    /** The login token could not be saved. */
    LOGIN_TOKEN_COULD_NOT_BE_SAVED(1044, "The login token could not be saved.", HttpStatus.INTERNAL_SERVER_ERROR),

    /** The user has not enabled MFA. */
    MFA_NOT_ENABLED(1045, "The user has not enabled MFA.", HttpStatus.BAD_REQUEST),

    /** The request body is invalid. */
    INVALID_REQUEST_BODY(1046, "The request body is invalid.", HttpStatus.BAD_REQUEST),

    /** The token is required. */
    TOKEN_REQUIRED(1047, "The token is required and it cannot be empty.", HttpStatus.BAD_REQUEST),

    /** The token is required. */
    PASSWORD_RESET_FAILED(1048, "Password could not be reset.", HttpStatus.INTERNAL_SERVER_ERROR),

    /** The password is recently updated. */
    PASSWORD_RECENTLY_UPDATED(1049, "Password was recently updated. Please try again later.", HttpStatus.TOO_MANY_REQUESTS),

    /** The password was previously used. */
    PASSWORD_ALREADY_USED(1050, "The specified password was previously used.", HttpStatus.BAD_REQUEST),

    /** The country is required. */
    COUNTRY_REQUIRED(1051, "The country is required.", HttpStatus.BAD_REQUEST),

    /** The region is required. */
    REGION_REQUIRED(1052, "The region is required.", HttpStatus.BAD_REQUEST),

    /** The culture is required. */
    CULTURE_REQUIRED(1053, "The culture is required.", HttpStatus.BAD_REQUEST),

    /** The session has expired. */
    SESSION_EXPIRED(1054, "The session has expired. Please log in again.", HttpStatus.UNAUTHORIZED),

    /** The auth header is invalid. */
    INVALID_AUTH_HEADER(1055, "Missing or invalid Authorization header.", HttpStatus.UNAUTHORIZED),

    /** The token is invalid or expired. */
    INVALID_EXPIRED_TOKEN(1056, "Invalid or expired Authentication token.", HttpStatus.UNAUTHORIZED),

    /** The terms and conditions are not accepted. */
    TERMS_AND_CONDITIONS_NOT_ACCEPTED(1057, "The terms and conditions are not accepted.", HttpStatus.BAD_REQUEST),

    /** The privacy policy is not accepted. */
    PRIVACY_POLICY_NOT_ACCEPTED(1058, "The privacy policy is not accepted.", HttpStatus.BAD_REQUEST),

    /** The phone code is required. */
    PHONE_CODE_REQUIRED(1059, "The phone code is required.", HttpStatus.BAD_REQUEST),

    /** The phone code is invalid. */
    INVALID_PHONE_CODE(1060, "The phone code is invalid.", HttpStatus.BAD_REQUEST),

    /** The uploaded file is empty. */
    FILE_IS_EMPTY(1061, "The uploaded file is empty.", HttpStatus.BAD_REQUEST),

    /** The uploaded file has exceeded the allowed size. */
    FILE_EXCEEDED_ALLOWED_SIZE(1062, "The uploaded file has exceeded the allowed size.", HttpStatus.BAD_REQUEST),

    /** The uploaded file has an invalid format. */
    INVALID_FILE_FORMAT(1063, "The uploaded file has an invalid format.", HttpStatus.BAD_REQUEST),

    /** There was an error with the storage service. */
    STORAGE_SERVICE_ERROR(1064, "There was an error with the storage service.", HttpStatus.INTERNAL_SERVER_ERROR),

    /** The avatar could not be updated. */
    AVATAR_COULD_NOT_BE_UPDATED(1065, "Avatar could not be updated.", HttpStatus.INTERNAL_SERVER_ERROR),

    /** The user identifier is required. */
    USER_IDENTIFIER_REQUIRED(1066, "User identifier is required.", HttpStatus.BAD_REQUEST);

    /** Custom numeric code for API-level identification. */
    private final int code;

    /** Default error message for client display. */
    private final String message;

    /** Corresponding HTTP status code. */
    private final HttpStatus status;
}