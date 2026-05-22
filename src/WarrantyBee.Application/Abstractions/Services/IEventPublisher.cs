namespace WarrantyBee.Application.Abstractions.Services;

/// <summary>
/// Defines a service for publishing events and notifications to the Event Manager.
/// </summary>
public interface IEventPublisher
{
    /// <summary>
    /// Publishes an event to the Event Manager asynchronously in a fire-and-forget manner.
    /// </summary>
    /// <param name="eventType">The type of the event (e.g., 'user.signup').</param>
    /// <param name="data">The event payload object (will be serialized to JSON).</param>
    /// <returns>A task representing the asynchronous operation.</returns>
    Task PublishAsync(string eventType, object data);
}
