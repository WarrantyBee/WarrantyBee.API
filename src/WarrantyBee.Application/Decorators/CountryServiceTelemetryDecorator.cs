using System.Diagnostics;
using WarrantyBee.Shared.Infrastructure.Abstractions;
using WarrantyBee.Application.Contracts.Geographic;
using WarrantyBee.Shared.Core.Enums;

using WarrantyBee.Application.Abstractions.Persistence;
using WarrantyBee.Application.Abstractions.Services;

namespace WarrantyBee.Application.Decorators;

/// <summary>
/// Decorator for <see cref="ICountryService"/> that adds telemetry, metrics, and error logging.
/// </summary>
public class CountryServiceTelemetryDecorator : ICountryService
{
    private readonly ICountryService _inner;
    private readonly ITelemetryService _telemetry;

    public CountryServiceTelemetryDecorator(ICountryService inner, ITelemetryService telemetry)
    {
        _inner = inner;
        _telemetry = telemetry;
    }

    public async Task<IEnumerable<CountryDetailResponse>> GetAsync()
    {
        return await ExecuteWithTelemetryAsync("Country.GetAll", () => _inner.GetAsync());
    }

    private async Task<T> ExecuteWithTelemetryAsync<T>(string operationName, Func<Task<T>> action, IDictionary<string, object>? context = null)
    {
        var sw = Stopwatch.StartNew();
        try
        {
            var result = await action();
            sw.Stop();
            
            _telemetry.TrackMetric($"{operationName}.Latency", sw.Elapsed.TotalMilliseconds, context);
            _telemetry.TrackEvent($"{operationName}.Success", context);
            
            return result;
        }
        catch (Exception ex)
        {
            sw.Stop();
            var errorContext = context ?? new Dictionary<string, object>();
            errorContext["Latency"] = sw.Elapsed.TotalMilliseconds;
            
            _telemetry.Log(LogLevel.Error, ex, errorContext);
            _telemetry.TrackEvent($"{operationName}.Failure", errorContext);
            throw;
        }
    }
}
