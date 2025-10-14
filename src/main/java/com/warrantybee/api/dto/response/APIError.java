package com.warrantybee.api.dto.response;

import lombok.Getter;

@Getter
public class APIError {
    public final int code;
    public final String message;

    public APIError(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
