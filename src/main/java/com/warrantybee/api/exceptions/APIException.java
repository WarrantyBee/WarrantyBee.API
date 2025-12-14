package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;
import com.warrantybee.api.helpers.Validator;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Custom exception class used across the API to represent standardized errors.
 * Wraps around {@link com.warrantybee.api.enumerations.Error} for consistent error handling.
 */
@Getter
public class APIException extends RuntimeException {

    /** Associated error metadata (code, message, status). */
    private final Error error;

    /** Optional custom message overriding the default error message. */
    private final String message;

    /**
     * Constructs an APIException using a predefined {@link Error}.
     *
     * @param error the predefined API error
     */
    public APIException(Error error) {
        super(error.getMessage());
        this.error = error;
        this.message = error.getMessage();
    }

    /**
     * Constructs an APIException using a predefined {@link Error} and a custom message.
     *
     * @param error the predefined API error
     * @param customMessage custom message for more context
     */
    public APIException(Error error, String customMessage) {
        super(customMessage);
        this.error = error;
        this.message = Validator.isBlank(customMessage) ? error.getMessage() : customMessage;
    }

    /**
     * Constructs an APIException using a predefined {@link Error} and a cause.
     *
     * @param error the predefined API error
     * @param cause the root cause exception
     */
    public APIException(Error error, Throwable cause) {
        super(error.getMessage(), cause);
        this.error = error;
        this.message = error.getMessage();
    }

    /**
     * Returns the associated HTTP status for this exception.
     *
     * @return corresponding {@link HttpStatus}
     */
    public HttpStatus getStatus() {
        return error.getStatus();
    }

    /**
     * Returns the standardized numeric error code.
     *
     * @return numeric error code
     */
    public int getCode() {
        return error.getCode();
    }
}
