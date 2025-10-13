package com.warrantybee.api.advices;

import com.warrantybee.api.mappers.ValidationErrorMapper;
import com.warrantybee.api.dto.response.APIError;
import com.warrantybee.api.dto.response.APIResponse;
import com.warrantybee.api.enumerations.ErrorDefinition;
import com.warrantybee.api.exceptions.APIException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<Void>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        // Take the first error for the field
        FieldError fieldError = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .orElse(null);

        APIError error;
        if (fieldError == null) {
            error = ErrorDefinition.INTERNAL_SERVER_ERROR.getError();
        } else {
            // Use centralized mapper
            error = ValidationErrorMapper.getError(fieldError.getField(), fieldError.getCode());
        }

        HttpStatus status = getHttpStatusCode(error);
        return ResponseEntity.status(status).body(new APIResponse<>(error));
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse<Void>> handleAPIException(APIException ex) {
        HttpStatus status = getHttpStatusCode(ex.getError());
        return ResponseEntity.status(status).body(new APIResponse<>(ex.getError()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<Void>> handleOtherExceptions(Exception ex) {
        APIError error = ErrorDefinition.INTERNAL_SERVER_ERROR.getError();
        HttpStatus status = getHttpStatusCode(error);
        return ResponseEntity.status(status).body(new APIResponse<>(error));
    }

    private HttpStatus getHttpStatusCode(APIError error) {
        if (error == null) return HttpStatus.INTERNAL_SERVER_ERROR;

        return switch (error.getCode()) {
            case 500 -> HttpStatus.INTERNAL_SERVER_ERROR;
            case 1000, 1001, 1002 -> HttpStatus.BAD_REQUEST;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
