using Polly;
using Polly.Retry;
using Polly.CircuitBreaker;

namespace WarrantyBee.Infrastructure.Resilience;

/// <summary>
/// Factory for creating standardized resilience policies using Polly.
/// </summary>
public static class ResiliencePolicies
{
    /// <summary>
    /// Creates an asynchronous retry policy with exponential backoff.
    /// </summary>
    /// <param name="retryCount">The number of times to retry.</param>
    /// <returns>An <see cref="AsyncRetryPolicy"/>.</returns>
    public static AsyncRetryPolicy CreateRetryPolicy(int retryCount = 3)
    {
        return Policy
            .Handle<Exception>()
            .WaitAndRetryAsync(
                retryCount,
                retryAttempt => TimeSpan.FromSeconds(Math.Pow(2, retryAttempt)),
                (exception, timeSpan, context) =>
                {
                    // Logging would happen here via a non-static logger if needed
                });
    }

    /// <summary>
    /// Creates an asynchronous circuit breaker policy.
    /// </summary>
    /// <param name="exceptionsAllowedBeforeBreaking">The number of exceptions allowed before the circuit breaks.</param>
    /// <param name="durationOfBreakSeconds">The duration the circuit stays open.</param>
    /// <returns>An <see cref="AsyncCircuitBreakerPolicy"/>.</returns>
    public static AsyncCircuitBreakerPolicy CreateCircuitBreakerPolicy(int exceptionsAllowedBeforeBreaking = 5, int durationOfBreakSeconds = 30)
    {
        return Policy
            .Handle<Exception>()
            .CircuitBreakerAsync(
                exceptionsAllowedBeforeBreaking,
                TimeSpan.FromSeconds(durationOfBreakSeconds)
            );
    }
}
