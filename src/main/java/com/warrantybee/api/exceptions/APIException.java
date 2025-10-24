package com.warrantybee.api.exceptions;

import com.warrantybee.api.dto.response.APIError;
import com.warrantybee.api.enumerations.ErrorDefinition;
import lombok.Getter;

@Getter
public class APIException extends RuntimeException {
    private final APIError error;

    public APIException() {
        this.error = ErrorDefinition.INTERNAL_SERVER_ERROR.getError();
    }

    public APIException(APIError error) {
        this.error = error;
    }
}
