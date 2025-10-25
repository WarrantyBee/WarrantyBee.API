package com.warrantybee.api.exceptions;

import com.warrantybee.api.enumerations.Error;

/**
 * Thrown when essential configuration values are missing,
 * misconfigured, or invalid during application startup or runtime.
 */
public class ConfigurationException extends APIException {

  /**
   * Constructs a new ConfigurationException with the default message
   * from {@link Error#INTERNAL_SERVER_ERROR}.
   */
  public ConfigurationException() {
    super(Error.INTERNAL_SERVER_ERROR);
  }

  /**
   * Constructs a new ConfigurationException with a custom message.
   *
   * @param message a detailed description of the configuration issue
   */
  public ConfigurationException(String message) {
    super(Error.INTERNAL_SERVER_ERROR, message);
  }

  /**
   * Constructs a new ConfigurationException with a custom message and cause.
   *
   * @param message a detailed description of the configuration issue
   * @param cause   the underlying cause of this exception (e.g., I/O or parsing error)
   */
  public ConfigurationException(String message, Throwable cause) {
    super(Error.INTERNAL_SERVER_ERROR, message);
    initCause(cause);
  }
}
