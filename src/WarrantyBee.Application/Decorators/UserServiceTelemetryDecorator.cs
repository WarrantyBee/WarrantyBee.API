using System.Diagnostics;
using WarrantyBee.Shared.Infrastructure.Abstractions;
using WarrantyBee.Application.Contracts.Users;
using WarrantyBee.Shared.Core.Enums;

using WarrantyBee.Application.Abstractions.Persistence;
using WarrantyBee.Application.Abstractions.Services;

namespace WarrantyBee.Application.Decorators;

/// <summary>
/// Decorator for <see cref="IUserService"/> that adds telemetry, metrics, and error logging.
/// </summary>
public class UserServiceTelemetryDecorator : IUserService
{
    private readonly IUserService _inner;
    private readonly ITelemetryService _telemetry;

    public UserServiceTelemetryDecorator(IUserService inner, ITelemetryService telemetry)
    {
        _inner = inner;
        _telemetry = telemetry;
    }

    public async Task<UserResponse?> GetAsync()
    {
        return await ExecuteWithTelemetryAsync("User.Get", () => _inner.GetAsync());
    }

    public async Task<AvatarResponse> ChangeAvatarAsync(long userId, Stream avatarStream, string fileName, string contentType)
    {
        return await ExecuteWithTelemetryAsync("User.ChangeAvatar", () => _inner.ChangeAvatarAsync(userId, avatarStream, fileName, contentType),
            new Dictionary<string, object> { ["UserId"] = userId, ["FileName"] = fileName });
    }

    public async Task UpdateProfileAsync(ProfileUpdateRequest request)
    {
        await ExecuteWithTelemetryAsync("User.UpdateProfile", async () => { await _inner.UpdateProfileAsync(request); return true; },
            new Dictionary<string, object> { ["UserId"] = request.UserId });
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
