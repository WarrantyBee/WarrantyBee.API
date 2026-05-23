using System.Text;
using System.Text.Json;
using Microsoft.Extensions.Logging;
using WarrantyBee.Application.Abstractions.Services;

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

    public async Task<string?> EnqueueNotificationAsync(string recipient, string templateName, IDictionary<string, string> macros)
    {
        var request = new
        {
            Recipient = recipient,
            TemplateName = templateName,
            Macros = macros
        };

        // Fire and forget via internal task queue
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
                    _logger.LogWarning("Failed to enqueue job in JobScheduler. Status: {StatusCode}", response.StatusCode);
                }
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Error occurred while calling JobScheduler.");
            }
        });

        return "queued-locally";
    }
}
