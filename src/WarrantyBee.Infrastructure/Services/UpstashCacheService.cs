using System.Net;
using System.Text.Json;
using Microsoft.Extensions.Options;
using RestSharp;
using WarrantyBee.Application.Abstractions.Services;
using WarrantyBee.Application.Configuration;

namespace WarrantyBee.Infrastructure.Services;

public class UpstashCacheService : ICacheService
{
    private readonly string _baseUrl;
    private readonly string _token;
    private readonly RestClient _client;

    public UpstashCacheService(IOptions<AppConfiguration> config)
    {
        var cfg = config.Value.Upstash ?? throw new ArgumentNullException(nameof(config));
        _baseUrl = cfg.Host;
        _token = cfg.AccessToken;
        _client = new RestClient(_baseUrl);
    }

    public async Task SetAsync(string key, string value, int? expirySeconds = null)
    {
        var command = new List<object> { "SET", key, value };
        if (expirySeconds.HasValue && expirySeconds.Value > 0)
        {
            command.Add("EX");
            command.Add(expirySeconds.Value);
        }

        await SendAsync(command);
    }

    public async Task<string?> GetAsync(string key)
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
    }

    public async Task DeleteAsync(string key)
    {
        var command = new List<object> { "DEL", key };
        await SendAsync(command);
    }

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
