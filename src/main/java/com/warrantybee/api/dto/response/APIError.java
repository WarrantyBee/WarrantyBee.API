package com.warrantybee.api.dto.response;

import lombok.Getter;

/** Represents an API error response */
@Getter
public class APIError {

    /** Error code */
    public final int code;

    /** Error message */
    public final String message;

    /** Constructs an APIError with code and message */
    public APIError(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
