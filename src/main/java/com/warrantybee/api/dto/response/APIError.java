package com.warrantybee.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Represents an API error response */
@Getter
@AllArgsConstructor
public class APIError {

    /** Error code */
    private final int code;

    /** Error message */
    private final String message;

    /** Private default constructor for internal use only. */
    private APIError() {
        this.code = -1;
        this.message = null;
    }
}
