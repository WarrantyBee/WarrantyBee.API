using WarrantyBee.Domain.Enums;

namespace WarrantyBee.Application.Abstractions.Services;

/// <summary>
/// Defines a service for scheduling background jobs in the Job Scheduler microservice.
/// </summary>
public interface IJobSchedulerClient
{
    /// <summary>
    /// Enqueues a notification job using the user ID and notification type.
    /// </summary>
    /// <param name="userId">The unique user ID.</param>
    /// <param name="type">The type of notification.</param>
    /// <param name="metadata">Optional dynamic metadata for the notification.</param>
    /// <returns>A task representing the asynchronous operation.</returns>
    Task<string?> EnqueueNotificationAsync(long userId, NotificationType type, IDictionary<string, string>? metadata = null);
}
