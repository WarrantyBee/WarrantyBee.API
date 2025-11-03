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

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler that intercepts exceptions thrown by controllers
 * and returns standardized API responses with proper HTTP status codes.
 * This class ensures a consistent error format across the API.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Service for logging telemetry data about exceptions.
     */
    private final ITelemetryService _telemetryService;

    /**
     * Constructs the GlobalExceptionHandler with a telemetry service dependency.
     * @param telemetryService The service used for logging exceptions and telemetry.
     */
    public GlobalExceptionHandler(ITelemetryService telemetryService) {
        this._telemetryService = telemetryService;
    }

    /**
     * Handles all custom API exceptions defined via {@link APIException} and its subclasses,
     * such as {@link InvalidTokenException} or {@link CacheException}.
     * It maps the custom exception's internal {@link Error} to an HTTP status code and response body.
     *
     * @param ex The {@code APIException} instance that was thrown.
     * @return A {@link ResponseEntity} containing a standardized {@link APIResponse} with the appropriate HTTP status and error details.
     */
    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse<Void>> handleAPIException(APIException ex) {
        Error error = ex.getError();
        APIError apiError = new APIError(error.getCode(), ex.getMessage());
        Map<String, Object> context = new HashMap<>();
        context.put("exception", ex);
        _telemetryService.log(LogLevel.ERROR, ex.getMessage(), context);
        return ResponseEntity.status(error.getStatus()).body(new APIResponse<>(apiError));
    }

    /**
     * Handles all other unhandled exceptions (like {@link RuntimeException}s or {@link java.io.IOException}s)
     * not explicitly managed by other handlers.
     * It logs the exception and returns a generic Internal Server Error (HTTP 500) response.
     *
     * @param ex The unhandled {@link Exception} instance.
     * @return A {@link ResponseEntity} with an HTTP 500 status and a generic internal server error message in the body.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<Void>> handleOtherExceptions(Exception ex) {
        Map<String, Object> context = new HashMap<>();
        context.put("exception", ex);
        _telemetryService.log(LogLevel.ERROR, ex.getMessage(), context);
        Error defaultError = Error.INTERNAL_SERVER_ERROR;
        APIError error = new APIError(defaultError.getCode(), "An unexpected error occurred.");
        return ResponseEntity.status(defaultError.getStatus()).body(new APIResponse<>(error));
    }
}