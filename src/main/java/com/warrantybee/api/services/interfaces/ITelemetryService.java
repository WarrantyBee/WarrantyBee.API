package com.warrantybee.api.services.interfaces;

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
     * Logs an exception or error.
     *
     * @param throwable the exception to log
     * @param context additional context information
     */
    void logError(Throwable throwable, Map<String, Object> context);

    /**
     * Records a metric with a numeric value.
     *
     * @param metricName the name of the metric
     * @param value the numeric value
     * @param properties additional properties to associate with the metric
     */
    void trackMetric(String metricName, double value, Map<String, Object> properties);

    /**
     * Flushes any buffered telemetry data to the telemetry backend.
     */
    void flush();
}
