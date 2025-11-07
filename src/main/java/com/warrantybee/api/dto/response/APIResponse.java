package com.warrantybee.api.dto.response;

import lombok.Getter;

/**
 * Represents a generic API response wrapper.
 * @param <T> the type of the response data
 */
@Getter
public class APIResponse<T> {

    /** The data returned in case of a successful response. This is {@code null} on failure. */
    private final T data;

    /** The error details returned in case of a failed response. This is {@code null} on success. */
    private final APIError error;

    /** Indicates whether the response represents a success (true) or failure (false). */
    private final boolean success;

    /**
     * Creates an API response with the given data and error.
     *
     * @param data  the response data
     * @param error the error details, null if successful
     */
    public APIResponse(T data, APIError error) {
        this.data = data;
        this.error = error;
        this.success = error == null;
    }

    /**
     * Constructs a successful API response with the specified data.
     * The {@code success} flag is set to {@code true} and {@code error} is {@code null}.
     *
     * @param data the response data to include
     */
    public APIResponse(T data) {
        this.data = data;
        this.success = true;
        this.error = null;
    }

    /**
     * Constructs an error API response with the specified error details.
     * The {@code success} flag is set to {@code false} and {@code data} is {@code null}.
     *
     * @param error the error details to include
     */
    public APIResponse(APIError error) {
        this.data = null;
        this.success = false;
        this.error = error;
    }
}
