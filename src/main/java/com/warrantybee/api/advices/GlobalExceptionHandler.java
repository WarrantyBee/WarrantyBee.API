package com.warrantybee.api.advices;

import com.warrantybee.api.dto.response.APIError;
import com.warrantybee.api.dto.response.APIResponse;
import com.warrantybee.api.enumerations.Error;
import com.warrantybee.api.enumerations.LogLevel;
import com.warrantybee.api.exceptions.APIException;
import com.warrantybee.api.exceptions.CacheException;
import com.warrantybee.api.exceptions.InvalidTokenException;
import com.warrantybee.api.services.interfaces.ITelemetryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler that intercepts exceptions thrown by controllers
 * and returns standardized API responses with proper HTTP status codes.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ITelemetryService _telemetryService;

    public GlobalExceptionHandler(ITelemetryService telemetryService) {
        this._telemetryService = telemetryService;
    }

    /**
     * Handles validation errors for request payloads annotated with @Valid.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<Void>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        // Take the first error for the field
        FieldError fieldError = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .orElse(null);

        APIError error;
        HttpStatus status;
        if (fieldError == null) {
            error = new APIError(Error.INTERNAL_SERVER_ERROR.getCode(), "Validation failed for one of the fields.");
            status = Error.INTERNAL_SERVER_ERROR.getStatus();
        } else {
            error = new APIError(Error.INVALID_INPUT.getCode(), "Validation failed for one of the fields.");
            status = Error.INVALID_INPUT.getStatus();
        }

        return ResponseEntity.status(status).body(new APIResponse<>(error));
    }

    /**
     * Handles all custom API exceptions defined via APIException and its subclasses.
     */
    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse<Void>> handleAPIException(APIException ex) {
        Error error = ex.getError();
        APIError apiError = new APIError(error.getCode(), ex.getMessage());
        return ResponseEntity.status(error.getStatus()).body(new APIResponse<>(apiError));
    }

    @ExceptionHandler(CacheException.class)
    public ResponseEntity<APIResponse<Void>> handleCacheException(CacheException ex) {
        _telemetryService.log(LogLevel.ERROR, ex, null);
        Error error = ex.getError();
        APIError apiError = new APIError(error.getCode(), ex.getMessage());
        return ResponseEntity.status(error.getStatus()).body(new APIResponse<>(apiError));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<APIResponse<Void>> handleInvalidTokenException(InvalidTokenException ex) {
        _telemetryService.log(LogLevel.ERROR, ex, null);
        Error error = ex.getError();
        APIError apiError = new APIError(error.getCode(), ex.getMessage());
        return ResponseEntity.status(error.getStatus()).body(new APIResponse<>(apiError));
    }

    /**
     * Handles all other unhandled exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<Void>> handleOtherExceptions(Exception ex) {
        Error defaultError = Error.INTERNAL_SERVER_ERROR;
        APIError error = new APIError(defaultError.getCode(), "An unexpected error occurred.");
        return ResponseEntity.status(defaultError.getStatus()).body(new APIResponse<>(error));
    }
}