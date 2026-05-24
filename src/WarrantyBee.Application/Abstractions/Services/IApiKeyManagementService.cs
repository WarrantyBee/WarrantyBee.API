using WarrantyBee.Domain.Entities;

namespace WarrantyBee.Shared.Infrastructure.Abstractions;

public interface IApiKeyManagementService
{
    Task<IEnumerable<ApiClient>> GetClientsAsync();
    Task<IEnumerable<ApiClient>> GetOwnedClientsAsync(long ownerUserId);
    Task<ApiClient> CreateClientAsync(string name, string description, byte appType, long ownerUserId);
    Task<ApiKeyResponse> GenerateKeyAsync(long clientId, int expiryMonths = 1);
    Task<IEnumerable<ApiKey>> GetKeysByClientAsync(long clientId);
    Task RevokeKeyAsync(long apiKeyId);
    Task UpdateKeyEndpointsAsync(long apiKeyId, IEnumerable<string> endpoints);
}

public class ApiKeyResponse
{
    public string AppId { get; set; } = string.Empty;
    public string AppSecret { get; set; } = string.Empty; // Plain text, returned only once
    public string ApiKey { get; set; } = string.Empty;    // Standalone API Key, returned only once
    public DateTime ExpiresAt { get; set; }
}
