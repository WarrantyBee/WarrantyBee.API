package com.warrantybee.api.mappers;

import com.warrantybee.api.dto.response.APIError;
import com.warrantybee.api.enumerations.ErrorDefinition;

import java.util.HashMap;
import java.util.Map;

public class ValidationErrorMapper {

    private static final Map<String, APIError> errorMap = new HashMap<>();

    static {
        errorMap.put("email.NotBlank", ErrorDefinition.EMAIL_REQUIRED.getError());
        errorMap.put("email.Email", ErrorDefinition.INVALID_EMAIL_FORMAT.getError());
        errorMap.put("password.NotBlank", ErrorDefinition.PASSWORD_REQUIRED.getError());
        errorMap.put("captchaResponse.NotBlank", ErrorDefinition.CAPTCHA_RESPONSE_REQUIRED.getError());
    }

    public static APIError getError(String field, String constraint) {
        return errorMap.getOrDefault(field + "." + constraint, ErrorDefinition.INTERNAL_SERVER_ERROR.getError());
    }
}
