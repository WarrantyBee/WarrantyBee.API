package com.warrantybee.api.services.interfaces;

import com.warrantybee.api.enumerations.LogLevel;

import java.util.Map;

/**
 * Interface for telemetry services to track events, errors, and performance metrics.
 */
public interface ITelemetryService {

    /**
     * Tracks a custom event with optional properties.
     *
     * @param eventName the name of the event
     * @param properties additional data associated with the event
     */
    void trackEvent(String eventName, Map<String, Object> properties);

    /**
     * Logs a message with a specific log level and context.
     *
     * @param level the log level
     * @param message the message to log
     * @param context additional context information
     */
    void log(LogLevel level, String message, Map<String, Object> context);

    /**
     * Logs an exception or error with a specific log level and context.
     *
     * @param level the log level
     * @param throwable the exception to log
     * @param context additional context information
     */
    void log(LogLevel level, Throwable throwable, Map<String, Object> context);

    /**
     * Records a metric with a numeric value.
     *
     * @param metricName the name of the metric
     * @param value the numeric value
     * @param properties additional properties to associate with the metric
     */
    void trackMetric(String metricName, double value, Map<String, Object> properties);

    /**
     * Flushes any buffered telemetry data, ensuring it is sent.
     */
    void flush();
}
