using Microsoft.Extensions.Logging;
using WarrantyBee.Application.Abstractions.Services;
using WarrantyBee.Domain.Enums;

namespace WarrantyBee.Infrastructure.Services;

/// <summary>
/// Provides telemetry and logging services, wrapping the standard .NET logger.
/// </summary>
public class TelemetryService : ITelemetryService
{
    private readonly ILogger<TelemetryService> _logger;

    /// <summary>
    /// Initializes a new instance of the <see cref="TelemetryService"/> class.
    /// </summary>
    /// <param name="logger">The .NET logger instance.</param>
    public TelemetryService(ILogger<TelemetryService> logger)
    {
        _logger = logger;
    }

    /// <summary>
    /// Tracks a custom event with optional properties.
    /// </summary>
    /// <param name="eventName">The name of the event.</param>
    /// <param name="properties">Optional properties associated with the event.</param>
    public void TrackEvent(string eventName, IDictionary<string, object>? properties = null)
    {
        _logger.LogInformation("Event: {EventName}, Properties: {@Properties}", eventName, properties);
    }

    /// <summary>
    /// Logs a message with a specified log level and optional context.
    /// </summary>
    /// <param name="level">The severity level of the log.</param>
    /// <param name="message">The message to log.</param>
    /// <param name="context">Optional context data for the log.</param>
    public void Log(Domain.Enums.LogLevel level, string message, IDictionary<string, object>? context = null)
    {
        var dotNetLevel = MapLevel(level);
        _logger.Log(dotNetLevel, "Message: {Message}, Context: {@Context}", message, context);
    }

    /// <summary>
    /// Logs an exception with a specified log level and optional context.
    /// </summary>
    /// <param name="level">The severity level of the log.</param>
    /// <param name="exception">The exception to log.</param>
    /// <param name="context">Optional context data for the log.</param>
    public void Log(Domain.Enums.LogLevel level, Exception exception, IDictionary<string, object>? context = null)
    {
        var dotNetLevel = MapLevel(level);
        _logger.Log(dotNetLevel, exception, "Exception, Context: {@Context}", context);
    }

    /// <summary>
    /// Tracks a numeric metric with optional properties.
    /// </summary>
    /// <param name="metricName">The name of the metric.</param>
    /// <param name="value">The value of the metric.</param>
    /// <param name="properties">Optional properties associated with the metric.</param>
    public void TrackMetric(string metricName, double value, IDictionary<string, object>? properties = null)
    {
        _logger.LogInformation("Metric: {MetricName}, Value: {Value}, Properties: {@Properties}", metricName, value, properties);
    }

    /// <summary>
    /// Flushes any buffered telemetry data.
    /// </summary>
    public void Flush() { }

    /// <summary>
    /// Maps the domain-specific log level to the standard .NET log level.
    /// </summary>
    /// <param name="level">The domain log level.</param>
    /// <returns>The corresponding .NET log level.</returns>
    private Microsoft.Extensions.Logging.LogLevel MapLevel(Domain.Enums.LogLevel level) => level switch
    {
        Domain.Enums.LogLevel.Info => Microsoft.Extensions.Logging.LogLevel.Information,
        Domain.Enums.LogLevel.Warn => Microsoft.Extensions.Logging.LogLevel.Warning,
        Domain.Enums.LogLevel.Error => Microsoft.Extensions.Logging.LogLevel.Error,
        Domain.Enums.LogLevel.Debug => Microsoft.Extensions.Logging.LogLevel.Debug,
        _ => Microsoft.Extensions.Logging.LogLevel.Information
    };
}
