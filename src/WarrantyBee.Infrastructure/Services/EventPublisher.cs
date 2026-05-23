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
    private readonly string? _appId;
    private readonly string? _appSecret;

    /// <summary>
    /// Initializes a new instance of the <see cref="EventPublisher"/> class.
    /// </summary>
    public EventPublisher(
        IHttpClientFactory httpClientFactory, 
        IBackgroundTaskQueue taskQueue,
        ILogger<EventPublisher> logger)
    {
        _httpClientFactory = httpClientFactory;
        _taskQueue = taskQueue;
        _logger = logger;
        
        _eventManagerUrl = Environment.GetEnvironmentVariable("WB__EVENT_MANAGER_URL") ?? "http://localhost:5000/api/events";
        
        // Load unified API credentials
        _appId = Environment.GetEnvironmentVariable("WB__APP_ID");
        _appSecret = Environment.GetEnvironmentVariable("WB__APP_SECRET");
    }

    /// <summary>
    /// Publishes an event to the Event Manager. 
    /// This implementation enqueues the HTTP call to a background queue to ensure it is truly non-blocking (Fire-and-Forget).
    /// </summary>
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
                
                // Add the Unified API Credentials
                if (!string.IsNullOrEmpty(_appId) && !string.IsNullOrEmpty(_appSecret))
                {
                    client.DefaultRequestHeaders.Add("X-APP-ID", _appId);
                    client.DefaultRequestHeaders.Add("X-APP-SECRET", _appSecret);
                }

                var json = JsonSerializer.Serialize(payload);
                var content = new StringContent(json, Encoding.UTF8, "application/json");

                var response = await client.PostAsync(_eventManagerUrl, content, token);

                if (!response.IsSuccessStatusCode)
                {
                    var error = await response.Content.ReadAsStringAsync(token);
                    _logger.LogWarning("Failed to publish event {EventType} to EventManager. Status: {StatusCode}, Error: {Error}", eventType, response.StatusCode, error);
                }
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Error occurred while publishing event {EventType} to EventManager.", eventType);
            }
        });
    }
}
