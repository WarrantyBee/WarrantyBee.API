package com.warrantybee.api.enumerations;

import com.warrantybee.api.dto.response.APIError;
import lombok.Getter;

@Getter
public enum ErrorDefinition {
    INTERNAL_SERVER_ERROR(new APIError(500, "Something went wrong.")),

    EMAIL_REQUIRED(new APIError(1000, "Email cannot be empty/")),
    INVALID_EMAIL_FORMAT(new APIError(1001, "Email has invalid format.")),
    PASSWORD_REQUIRED(new APIError(1002, "Password cannot be empty.")),
    CAPTCHA_RESPONSE_REQUIRED(new APIError(1003, "Captcha response cannot be empty.")),
    INVALID_CREDENTIALS(new APIError(1004, "Given login credentials are invalid.")),
    USER_NOT_FOUND(new APIError(1005, "User not found."));

    private final APIError error;

    ErrorDefinition(APIError error) {
        this.error = error;
    }
}
