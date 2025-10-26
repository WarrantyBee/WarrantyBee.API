package com.warrantybee.api.enumerations;

/**
 * Represents the different levels of logging severity
 * used throughout the application.
 */
public enum LogLevel {

    /**
     * Informational messages that highlight
     * the normal progress of the application.
     */
    INFO,

    /**
     * Indicates a potentially harmful situation
     * or something that may require attention but
     * is not an error.
     */
    WARN,

    /**
     * Represents error events that may still allow
     * the application to continue running.
     */
    ERROR,

    /**
     * Fine-grained informational events useful
     * for debugging and development.
     */
    DEBUG
}
