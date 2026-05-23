using System.Net;
using System.Text.Json;
using Microsoft.Extensions.Options;
using RestSharp;
using WarrantyBee.Application.Abstractions.Services;
using WarrantyBee.Application.Configuration;
using WarrantyBee.Infrastructure.Resilience;
using Polly.Retry;

namespace WarrantyBee.Infrastructure.Services;

/// <summary>
/// Provides caching services using Upstash (Redis over HTTP) as the backend, with Polly resilience.
/// </summary>
public class UpstashCacheService : ICacheService
{
    private readonly string _baseUrl;
    private readonly string _token;
    private readonly RestClient _client;
    private readonly AsyncRetryPolicy _retryPolicy;

    /// <summary>
    /// Initializes a new instance of the <see cref="UpstashCacheService"/> class.
    /// </summary>
    /// <param name="config">The application configuration containing Upstash settings.</param>
    /// <exception cref="ArgumentNullException">Thrown when configuration or Upstash settings are missing.</exception>
    public UpstashCacheService(IOptions<AppConfiguration> config)
    {
        var cfg = config.Value.Upstash ?? throw new ArgumentNullException(nameof(config));
        _baseUrl = cfg.Host;
        _token = cfg.AccessToken;
        _client = new RestClient(_baseUrl);
        
        // High-Scale: Initialize retry policy for transient network failures
        _retryPolicy = ResiliencePolicies.CreateRetryPolicy();
    }

    /// <summary>
    /// Sets a value in the cache with an optional expiration.
    /// </summary>
    /// <param name="key">The cache key.</param>
    /// <param name="value">The value to store.</param>
    /// <param name="expirySeconds">Optional expiration time in seconds.</param>
    /// <returns>A task representing the asynchronous operation.</returns>
    public async Task SetAsync(string key, string value, int? expirySeconds = null)
    {
        var command = new List<object> { "SET", key, value };
        if (expirySeconds.HasValue && expirySeconds.Value > 0)
        {
            command.Add("EX");
            command.Add(expirySeconds.Value);
        }

        await _retryPolicy.ExecuteAsync(() => SendAsync(command));
    }

    /// <summary>
    /// Retrieves a value from the cache by its key.
    /// </summary>
    /// <param name="key">The cache key.</param>
    /// <returns>The stored value if found; otherwise, <c>null</c>.</returns>
    /// <exception cref="Exception">Thrown when Upstash returns an error.</exception>
    public async Task<string?> GetAsync(string key)
    {
        return await _retryPolicy.ExecuteAsync(async () =>
        {
            var command = new List<object> { "GET", key };
            var response = await SendAsync(command);
            
            using var doc = JsonDocument.Parse(response);
            if (doc.RootElement.TryGetProperty("error", out var error))
            {
                throw new Exception($"Upstash error: {error.GetString()}");
            }

            if (doc.RootElement.TryGetProperty("result", out var result) && result.ValueKind != JsonValueKind.Null)
            {
                return result.GetString();
            }

            return null;
        });
    }

    /// <summary>
    /// Deletes a value from the cache by its key.
    /// </summary>
    /// <param name="key">The cache key.</param>
    /// <returns>A task representing the asynchronous operation.</returns>
    public async Task DeleteAsync(string key)
    {
        var command = new List<object> { "DEL", key };
        await _retryPolicy.ExecuteAsync(() => SendAsync(command));
    }

    /// <summary>
    /// Sends a Redis command to Upstash via HTTP.
    /// </summary>
    /// <param name="command">The list representing the Redis command and its arguments.</param>
    /// <returns>The raw JSON response string from Upstash.</returns>
    /// <exception cref="UnauthorizedAccessException">Thrown when the Upstash token is invalid.</exception>
    /// <exception cref="Exception">Thrown when the request fails.</exception>
    private async Task<string> SendAsync(List<object> command)
    {
        var request = new RestRequest("/", Method.Post);
        request.AddHeader("Authorization", $"Bearer {_token}");
        request.AddJsonBody(command);

        var response = await _client.ExecuteAsync(request);

        if (response.StatusCode == HttpStatusCode.Unauthorized)
            throw new UnauthorizedAccessException("Invalid Upstash credentials.");
        
        if (!response.IsSuccessful)
            throw new Exception($"Upstash request failed with status {response.StatusCode}: {response.Content}");

        return response.Content ?? string.Empty;
    }
}
