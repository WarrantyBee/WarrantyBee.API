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
}
