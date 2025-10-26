package com.warrantybee.api.dto.response;

import lombok.Getter;

/** Generic API response wrapper */
@Getter
public class APIResponse<T> {

    /** Response data */
    private final T data;

    /** Error details */
    private final APIError error;

    /** Success flag */
    private final boolean success;

    /** Creates a successful response */
    public APIResponse(T data) {
        this.data = data;
        this.success = true;
        this.error = null;
    }

    /** Creates an error response */
    public APIResponse(APIError error) {
        this.data = null;
        this.success = false;
        this.error = error;
    }
}
