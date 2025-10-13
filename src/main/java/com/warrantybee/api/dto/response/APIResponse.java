package com.warrantybee.api.dto.response;

import lombok.Getter;

@Getter
public class APIResponse<T> {
    private final T data;
    private final APIError error;
    private final boolean success;

    public APIResponse(T data) {
        this.data = data;
        this.success = true;
        this.error = null;
    }

    public APIResponse(APIError error) {
        this.data = null;
        this.success = false;
        this.error = error;
    }
}
