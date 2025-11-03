package com.warrantybee.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Represents an API error response */
@Getter
@AllArgsConstructor
public class APIError {

    /** The specific HTTP status code or application-defined error code. */
    private final int code;

    /** A human-readable message describing the error. */
    private final String message;

    /**
     * Private default constructor for internal use only.
     * Initializes code to -1 and message to null.
     */
    private APIError() {
        this.code = -1;
        this.message = null;
    }
}
