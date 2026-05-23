using System.Diagnostics;
using WarrantyBee.Shared.Infrastructure.Abstractions;
using WarrantyBee.Application.Contracts.Identity;
using WarrantyBee.Shared.Core.Enums;
using WarrantyBee.Application.Abstractions.Services;

namespace WarrantyBee.Application.Decorators;

/// <summary>
/// Decorator for <see cref="IAuthService"/> that adds telemetry, metrics, and error logging.
/// </summary>
public class AuthServiceTelemetryDecorator : IAuthService
{
    private readonly IAuthService _inner;
    private readonly ITelemetryService _telemetry;

    public AuthServiceTelemetryDecorator(IAuthService inner, ITelemetryService telemetry)
    {
        _inner = inner;
        _telemetry = telemetry;
    }

    public async Task<ILoginResponse> LoginAsync(LoginRequest request)
    {
        return await ExecuteWithTelemetryAsync("Auth.Login", () => _inner.LoginAsync(request), 
            new Dictionary<string, object> { ["Email"] = request.Email });
    }

    public async Task<SignUpResponse> SignUpAsync(SignUpRequest request)
    {
        return await ExecuteWithTelemetryAsync("Auth.SignUp", () => _inner.SignUpAsync(request), 
            new Dictionary<string, object> { ["Email"] = request.Email });
    }

    public async Task ForgotPasswordAsync(ForgotPasswordRequest request)
    {
        await ExecuteWithTelemetryAsync("Auth.ForgotPassword", async () => { await _inner.ForgotPasswordAsync(request); return true; }, 
            new Dictionary<string, object> { ["Email"] = request.Email });
    }

    public async Task ResetPasswordAsync(ResetPasswordRequest request)
    {
        await ExecuteWithTelemetryAsync("Auth.ResetPassword", async () => { await _inner.ResetPasswordAsync(request); return true; }, 
            new Dictionary<string, object> { ["Email"] = request.Email });
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
