using System.Threading.Channels;

namespace WarrantyBee.Application.Abstractions.Services;

/// <summary>
/// Defines a thread-safe background task queue for offloading non-critical operations.
/// </summary>
public interface IBackgroundTaskQueue
{
    /// <summary>
    /// Enqueues an asynchronous work item to the background queue.
    /// </summary>
    /// <param name="workItem">The asynchronous function representing the work item.</param>
    /// <returns>A value task representing the asynchronous operation.</returns>
    ValueTask QueueBackgroundWorkItemAsync(Func<CancellationToken, ValueTask> workItem);

    /// <summary>
    /// Dequeues an asynchronous work item from the background queue.
    /// </summary>
    /// <param name="cancellationToken">A token used to cancel the dequeue operation.</param>
    /// <returns>The dequeued work item.</returns>
    ValueTask<Func<CancellationToken, ValueTask>> DequeueAsync(CancellationToken cancellationToken);
}

/// <summary>
/// Implementation of <see cref="IBackgroundTaskQueue"/> using <see cref="Channel{T}"/>.
/// </summary>
public class BackgroundTaskQueue : IBackgroundTaskQueue
{
    private readonly Channel<Func<CancellationToken, ValueTask>> _queue;

    public BackgroundTaskQueue(int capacity = 1000)
    {
        // Bounded channel to prevent memory exhaustion under extreme load
        var options = new BoundedChannelOptions(capacity)
        {
            FullMode = BoundedChannelFullMode.Wait
        };
        _queue = Channel.CreateBounded<Func<CancellationToken, ValueTask>>(options);
    }

    public async ValueTask QueueBackgroundWorkItemAsync(Func<CancellationToken, ValueTask> workItem)
    {
        if (workItem == null) throw new ArgumentNullException(nameof(workItem));
        await _queue.Writer.WriteAsync(workItem);
    }

    public async ValueTask<Func<CancellationToken, ValueTask>> DequeueAsync(CancellationToken cancellationToken)
    {
        return await _queue.Reader.ReadAsync(cancellationToken);
    }
}
