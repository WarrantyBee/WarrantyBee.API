using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;
using WarrantyBee.Application.Abstractions.Services;

namespace WarrantyBee.Infrastructure.Background;

/// <summary>
/// A background hosted service that processes work items from the <see cref="IBackgroundTaskQueue"/>.
/// </summary>
public class QueuedHostedService : BackgroundService
{
    private readonly ILogger<QueuedHostedService> _logger;
    private readonly IBackgroundTaskQueue _taskQueue;

    public QueuedHostedService(IBackgroundTaskQueue taskQueue, ILogger<QueuedHostedService> logger)
    {
        _taskQueue = taskQueue;
        _logger = logger;
    }

    protected override async Task ExecuteAsync(CancellationToken stoppingToken)
    {
        _logger.LogInformation("Queued Hosted Service is starting.");

        while (!stoppingToken.IsCancellationRequested)
        {
            var workItem = await _taskQueue.DequeueAsync(stoppingToken);

            try
            {
                await workItem(stoppingToken);
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Error occurred executing background work item.");
            }
        }

        _logger.LogInformation("Queued Hosted Service is stopping.");
    }
}
