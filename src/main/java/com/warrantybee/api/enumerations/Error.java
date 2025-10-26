package com.warrantybee.api.enumerations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Represents standardized error codes used across the API.
 * Each code includes a numeric identifier, default message, and associated HTTP status.
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
    INVALID_CAPTCHA(1015, "Invalid captcha.", HttpStatus.BAD_REQUEST),

    /** The specified user is not registered. */
    USER_NOT_REGISTERED(1016, "The specified user is not registered.", HttpStatus.NOT_FOUND);

    /** Custom numeric code for API-level identification. */
    private final int code;

    /** Default error message for client display. */
    private final String message;

    /** Corresponding HTTP status code. */
    private final HttpStatus status;
}