using Microsoft.AspNetCore.Mvc.Filters;
using System.Diagnostics;
using WarrantyBee.Application.Abstractions.Services;
using WarrantyBee.Domain.Enums;

namespace WarrantyBee.Api.Filters;

/// <summary>
/// A global action filter that tracks request latency and logs endpoint execution metadata.
/// </summary>
public class TelemetryActionFilter : IAsyncActionFilter
{
    private readonly ITelemetryService _telemetry;

    public TelemetryActionFilter(ITelemetryService telemetry)
    {
        _telemetry = telemetry;
    }

    public async Task OnActionExecutionAsync(ActionExecutingContext context, ActionExecutionDelegate next)
    {
        var stopwatch = Stopwatch.StartNew();
        var actionName = context.ActionDescriptor.DisplayName ?? "UnknownAction";
        var httpMethod = context.HttpContext.Request.Method;
        var path = context.HttpContext.Request.Path;

        var telemetryContext = new Dictionary<string, object>
        {
            ["Action"] = actionName,
            ["Method"] = httpMethod,
            ["Path"] = path
        };

        try
        {
            var resultContext = await next();
            stopwatch.Stop();

            telemetryContext["Duration"] = stopwatch.Elapsed.TotalMilliseconds;
            telemetryContext["StatusCode"] = context.HttpContext.Response.StatusCode;

            if (resultContext.Exception != null && !resultContext.ExceptionHandled)
            {
                _telemetry.Log(WarrantyBee.Domain.Enums.LogLevel.Error, resultContext.Exception, telemetryContext);
                _telemetry.TrackEvent($"Http.{actionName}.Failure", telemetryContext);
            }
            else
            {
                _telemetry.TrackMetric($"Http.{actionName}.Latency", stopwatch.Elapsed.TotalMilliseconds, telemetryContext);
                _telemetry.TrackEvent($"Http.{actionName}.Success", telemetryContext);
            }
        }
        catch (Exception ex)
        {
            stopwatch.Stop();
            telemetryContext["Duration"] = stopwatch.Elapsed.TotalMilliseconds;
            _telemetry.Log(WarrantyBee.Domain.Enums.LogLevel.Error, ex, telemetryContext);
            throw;
        }
    }
}
