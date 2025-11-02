package com.warrantybee.api.exceptions;

public class CustomProcessException extends RuntimeException {
    public CustomProcessException(String message) {
      super(message);
    }

  public CustomProcessException(String message, Throwable cause) {
    super(message, cause);
  }
}
