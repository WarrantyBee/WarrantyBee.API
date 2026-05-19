using Microsoft.Extensions.Logging;
using WarrantyBee.Application.Abstractions.Services;
using WarrantyBee.Domain.Enums;

namespace WarrantyBee.Infrastructure.Services;

public class TelemetryService : ITelemetryService
{
    private readonly ILogger<TelemetryService> _logger;

    public TelemetryService(ILogger<TelemetryService> logger)
    {
        _logger = logger;
    }

    public void TrackEvent(string eventName, IDictionary<string, object>? properties = null)
    {
        _logger.LogInformation("Event: {EventName}, Properties: {@Properties}", eventName, properties);
    }

    public void Log(Domain.Enums.LogLevel level, string message, IDictionary<string, object>? context = null)
    {
        var dotNetLevel = MapLevel(level);
        _logger.Log(dotNetLevel, "Message: {Message}, Context: {@Context}", message, context);
    }

    public void Log(Domain.Enums.LogLevel level, Exception exception, IDictionary<string, object>? context = null)
    {
        var dotNetLevel = MapLevel(level);
        _logger.Log(dotNetLevel, exception, "Exception, Context: {@Context}", context);
    }

    public void TrackMetric(string metricName, double value, IDictionary<string, object>? properties = null)
    {
        _logger.LogInformation("Metric: {MetricName}, Value: {Value}, Properties: {@Properties}", metricName, value, properties);
    }

    public void Flush() { }

    private Microsoft.Extensions.Logging.LogLevel MapLevel(Domain.Enums.LogLevel level) => level switch
    {
        Domain.Enums.LogLevel.Info => Microsoft.Extensions.Logging.LogLevel.Information,
        Domain.Enums.LogLevel.Warn => Microsoft.Extensions.Logging.LogLevel.Warning,
        Domain.Enums.LogLevel.Error => Microsoft.Extensions.Logging.LogLevel.Error,
        Domain.Enums.LogLevel.Debug => Microsoft.Extensions.Logging.LogLevel.Debug,
        _ => Microsoft.Extensions.Logging.LogLevel.Information
    };
}
