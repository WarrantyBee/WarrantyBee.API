package com.warrantybee.api.dto.response;

import lombok.Getter;

/**
 * Represents a generic API response wrapper.
 * @param <T> the type of the response data
 */
@Getter
public class APIResponse<T> {

    /** The data returned in case of a successful response. */
    private final T data;

    /** The error details returned in case of a failed response. */
    private final APIError error;

    /** Indicates whether the response represents a success or failure. */
    private final boolean success;

    /** Private default constructor for internal use only. */
    private APIResponse() {
        this.data = null;
        this.error = null;
        this.success = false;
    }

    /**
     * Constructs a successful API response with the specified data.
     * @param data the response data to include
     */
    public APIResponse(T data) {
        this.data = data;
        this.success = true;
        this.error = null;
    }

    /**
     * Constructs an error API response with the specified error details.
     * @param error the error details to include
     */
    public APIResponse(APIError error) {
        this.data = null;
        this.success = false;
        this.error = error;
    }
}
