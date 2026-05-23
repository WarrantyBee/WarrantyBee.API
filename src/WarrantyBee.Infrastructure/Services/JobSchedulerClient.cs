using System.Text;
using System.Text.Json;
using Microsoft.Extensions.Logging;
using WarrantyBee.Application.Abstractions.Services;
using WarrantyBee.Domain.Enums;

namespace WarrantyBee.Infrastructure.Services;

/// <summary>
/// Implementation of <see cref="IJobSchedulerClient"/> that communicates with the Job Scheduler microservice.
/// </summary>
public class JobSchedulerClient : IJobSchedulerClient
{
    private readonly IHttpClientFactory _httpClientFactory;
    private readonly IBackgroundTaskQueue _taskQueue;
    private readonly ILogger<JobSchedulerClient> _logger;
    private readonly string _jobSchedulerUrl;

    public JobSchedulerClient(
        IHttpClientFactory httpClientFactory,
        IBackgroundTaskQueue taskQueue,
        ILogger<JobSchedulerClient> logger)
    {
        _httpClientFactory = httpClientFactory;
        _taskQueue = taskQueue;
        _logger = logger;
        _jobSchedulerUrl = Environment.GetEnvironmentVariable("WB__JOB_SCHEDULER_URL") ?? "http://localhost:5001/api/jobs";
    }

    public async Task<string?> EnqueueNotificationAsync(long userId, NotificationType type, IDictionary<string, string>? metadata = null)
    {
        var request = new
        {
            UserId = userId,
            Type = (int)type,
            Metadata = metadata
        };

        // Fire and forget via internal task queue to ensure no latency for the caller
        await _taskQueue.QueueBackgroundWorkItemAsync(async token =>
        {
            try
            {
                var client = _httpClientFactory.CreateClient();
                var json = JsonSerializer.Serialize(request);
                var content = new StringContent(json, Encoding.UTF8, "application/json");

                var response = await client.PostAsync($"{_jobSchedulerUrl}/notification", content, token);

                if (!response.IsSuccessStatusCode)
                {
                    var error = await response.Content.ReadAsStringAsync(token);
                    _logger.LogWarning("Failed to enqueue job in JobScheduler. Status: {StatusCode}, Error: {Error}", response.StatusCode, error);
                }
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Error occurred while calling JobScheduler for User {UserId}.", userId);
            }
        });

        return "queued-locally";
    }
}
