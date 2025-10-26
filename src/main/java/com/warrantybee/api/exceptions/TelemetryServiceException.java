package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when an error occurs while interacting with the telemetry service.
 */
public class TelemetryServiceException extends APIException {

  /**
   * Constructs a new TelemetryServiceException with the default message
   * from {@link Error#TELEMETRY_SERVICE_ERROR}.
   */
  public TelemetryServiceException() {
    super(Error.TELEMETRY_SERVICE_ERROR);
  }

  /**
   * Constructs a new TelemetryServiceException with a custom message.
   *
   * @param message a detailed description of the telemetry service error
   */
  public TelemetryServiceException(String message) {
    super(Error.TELEMETRY_SERVICE_ERROR, message);
  }

  /**
   * Constructs a new TelemetryServiceException with a custom message and cause.
   *
   * @param message a detailed description of the telemetry service error
   * @param cause   the underlying cause of this exception
   */
  public TelemetryServiceException(String message, Throwable cause) {
    super(Error.TELEMETRY_SERVICE_ERROR, message);
    initCause(cause);
  }
}
