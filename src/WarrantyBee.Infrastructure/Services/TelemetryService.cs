using System.Net;
using System.Text.Json;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;
using RestSharp;
using WarrantyBee.Application.Abstractions.Services;
using WarrantyBee.Application.Configuration;
using WarrantyBee.Domain.Enums;

namespace WarrantyBee.Infrastructure.Services;

/// <summary>
/// Provides telemetry and logging services, integrating with Better Stack for remote log ingestion.
/// </summary>
public class TelemetryService : ITelemetryService
{
    private readonly ILogger<TelemetryService> _logger;
    private readonly BetterStackConfiguration? _config;
    private readonly RestClient? _client;

    /// <summary>
    /// Initializes a new instance of the <see cref="TelemetryService"/> class.
    /// </summary>
    /// <param name="logger">The .NET logger instance.</param>
    /// <param name="config">The application configuration containing Better Stack settings.</param>
    public TelemetryService(ILogger<TelemetryService> logger, IOptions<AppConfiguration> config)
    {
        _logger = logger;
        _config = config.Value.BetterStack;

        if (_config != null && !string.IsNullOrWhiteSpace(_config.Host) && !string.IsNullOrWhiteSpace(_config.AccessToken))
        {
            var host = _config.Host.StartsWith("http") ? _config.Host : $"https://{_config.Host}";
            _client = new RestClient(host);
        }
    }

    /// <summary>
    /// Tracks a custom event with optional properties.
    /// </summary>
    /// <param name="eventName">The name of the event.</param>
    /// <param name="properties">Optional properties associated with the event.</param>
    public void TrackEvent(string eventName, IDictionary<string, object>? properties = null)
    {
        _logger.LogInformation("Event: {EventName}, Properties: {@Properties}", eventName, properties);
        SendToBetterStack(Domain.Enums.LogLevel.Info, $"Event: {eventName}", properties);
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
        SendToBetterStack(level, message, context);
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
        
        var extendedContext = context ?? new Dictionary<string, object>();
        extendedContext["Exception"] = exception.Message;
        extendedContext["StackTrace"] = exception.StackTrace ?? string.Empty;
        
        SendToBetterStack(level, exception.Message, extendedContext);
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
        
        var context = properties ?? new Dictionary<string, object>();
        context["MetricValue"] = value;
        SendToBetterStack(Domain.Enums.LogLevel.Info, $"Metric: {metricName}", context);
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

    /// <summary>
    /// Sends a log entry to Better Stack via HTTP.
    /// </summary>
    /// <param name="level">The log level.</param>
    /// <param name="message">The log message.</param>
    /// <param name="context">Optional context data.</param>
    private async void SendToBetterStack(Domain.Enums.LogLevel level, string message, IDictionary<string, object>? context = null)
    {
        if (_client == null || _config == null) return;

        try
        {
            var request = new RestRequest("/", Method.Post);
            request.AddHeader("Authorization", $"Bearer {_config.AccessToken}");
            
            var payload = new
            {
                dt = DateTime.UtcNow.ToString("O"),
                level = level.ToString(),
                message = message,
                metadata = context
            };

            request.AddJsonBody(payload);
            await _client.ExecuteAsync(request);
        }
        catch
        {
            // Fail silently to avoid interrupting the main application flow
        }
    }
}
