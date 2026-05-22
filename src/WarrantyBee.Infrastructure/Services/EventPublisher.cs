using System.Text;
using System.Text.Json;
using Microsoft.Extensions.Logging;
using WarrantyBee.Application.Abstractions.Services;

namespace WarrantyBee.Infrastructure.Services;

/// <summary>
/// Implementation of <see cref="IEventPublisher"/> that sends events to the EventManager microservice.
/// </summary>
public class EventPublisher : IEventPublisher
{
    private readonly IHttpClientFactory _httpClientFactory;
    private readonly IBackgroundTaskQueue _taskQueue;
    private readonly ILogger<EventPublisher> _logger;
    private readonly string _eventManagerUrl;

    /// <summary>
    /// Initializes a new instance of the <see cref="EventPublisher"/> class.
    /// </summary>
    /// <param name="httpClientFactory">The HTTP client factory.</param>
    /// <param name="taskQueue">The background task queue for fire-and-forget processing.</param>
    /// <param name="logger">The logger.</param>
    public EventPublisher(
        IHttpClientFactory httpClientFactory, 
        IBackgroundTaskQueue taskQueue,
        ILogger<EventPublisher> logger)
    {
        _httpClientFactory = httpClientFactory;
        _taskQueue = taskQueue;
        _logger = logger;
        
        // This would typically come from configuration/environment variables
        _eventManagerUrl = Environment.GetEnvironmentVariable("WB__EVENT_MANAGER_URL") ?? "http://localhost:5000/api/events";
    }

    /// <summary>
    /// Publishes an event to the Event Manager. 
    /// This implementation enqueues the HTTP call to a background queue to ensure it is truly non-blocking (Fire-and-Forget).
    /// </summary>
    /// <param name="eventType">The type of the event.</param>
    /// <param name="data">The event payload.</param>
    public async Task PublishAsync(string eventType, object data)
    {
        var payload = new
        {
            Type = eventType,
            Data = JsonSerializer.Serialize(data),
            Timestamp = DateTime.UtcNow
        };

        // Enqueue the work item to the background queue
        await _taskQueue.QueueBackgroundWorkItemAsync(async token =>
        {
            try
            {
                var client = _httpClientFactory.CreateClient();
                var json = JsonSerializer.Serialize(payload);
                var content = new StringContent(json, Encoding.UTF8, "application/json");

                var response = await client.PostAsync(_eventManagerUrl, content, token);

                if (!response.IsSuccessStatusCode)
                {
                    _logger.LogWarning("Failed to publish event {EventType} to EventManager. Status: {StatusCode}", eventType, response.StatusCode);
                }
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Error occurred while publishing event {EventType} to EventManager.", eventType);
            }
        });
    }
}
