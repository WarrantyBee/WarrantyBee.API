using WarrantyBee.Domain.Entities;

namespace WarrantyBee.Application.Abstractions.Services;

public interface IApiKeyManagementService
{
    Task<IEnumerable<ApiClient>> GetClientsAsync();
    Task<ApiKeyResponse> GenerateKeyAsync(long clientId, int expiryMonths = 1);
    Task<IEnumerable<ApiKey>> GetKeysByClientAsync(long clientId);
    Task RevokeKeyAsync(long apiKeyId);
}

public class ApiKeyResponse
{
    public string AppId { get; set; } = string.Empty;
    public string AppSecret { get; set; } = string.Empty; // Plain text, returned only once
    public DateTime ExpiresAt { get; set; }
}
