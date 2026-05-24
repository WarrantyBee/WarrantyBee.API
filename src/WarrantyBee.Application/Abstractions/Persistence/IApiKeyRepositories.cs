using WarrantyBee.Domain.Entities;

namespace WarrantyBee.Application.Abstractions.Persistence;

public interface IApiClientRepository
{
    Task<ApiClient?> GetByAppIdAsync(string appId);
    Task<IEnumerable<ApiClient>> GetAllAsync();
    Task<long> CreateAsync(ApiClient client);
}

public interface IApiKeyRepository
{
    Task<ApiKey?> GetByHashAsync(string appId, string secretHash);
    Task<IEnumerable<ApiKey>> GetByClientIdAsync(long clientId);
    Task<long> CreateAsync(ApiKey apiKey);
    Task RevokeAsync(long id);
    Task DeleteExpiredAsync();
    Task UpdateEndpointsAsync(long keyId, IEnumerable<string> endpoints);
}
