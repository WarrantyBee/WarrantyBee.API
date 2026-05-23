namespace WarrantyBee.Application.Abstractions.Services;

/// <summary>
/// Defines a service for scheduling background jobs in the Job Scheduler microservice.
/// </summary>
public interface IJobSchedulerClient
{
    /// <summary>
    /// Enqueues an email notification job.
    /// </summary>
    /// <param name="recipient">The recipient email address.</param>
    /// <param name="templateName">The name of the template to use.</param>
    /// <param name="macros">The dynamic macros to replace in the template.</param>
    /// <returns>A task representing the asynchronous operation.</returns>
    Task<string?> EnqueueNotificationAsync(string recipient, string templateName, IDictionary<string, string> macros);
}
