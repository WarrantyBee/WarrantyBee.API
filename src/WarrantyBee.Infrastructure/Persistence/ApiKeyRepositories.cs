using System.Data;
using Dapper;
using WarrantyBee.Application.Abstractions.Persistence;
using WarrantyBee.Domain.Entities;

namespace WarrantyBee.Infrastructure.Persistence;

public class ApiClientRepository : IApiClientRepository
{
    private readonly IDbConnectionFactory _connectionFactory;

    public ApiClientRepository(IDbConnectionFactory connectionFactory)
    {
        _connectionFactory = connectionFactory;
    }

    public async Task<ApiClient?> GetByAppIdAsync(string appId)
    {
        using var connection = _connectionFactory.CreateConnection();
        return await connection.QueryFirstOrDefaultAsync<ApiClient>(
            "SELECT id AS Id, app_id AS AppId, name AS Name, description AS Description FROM tblApiClients WHERE app_id = @appId AND void = 0", 
            new { appId });
    }

    public async Task<IEnumerable<ApiClient>> GetAllAsync()
    {
        using var connection = _connectionFactory.CreateConnection();
        return await connection.QueryAsync<ApiClient>(
            "SELECT id AS Id, app_id AS AppId, name AS Name, description AS Description FROM tblApiClients WHERE void = 0");
    }

    public async Task<long> CreateAsync(ApiClient client)
    {
        using var connection = _connectionFactory.CreateConnection();
        var sql = @"INSERT INTO tblApiClients (internal_id, app_id, name, description, created_at, void) 
                    VALUES (NEWID(), @AppId, @Name, @Description, GETUTCDATE(), 0);
                    SELECT CAST(SCOPE_IDENTITY() as BIGINT);";
        return await connection.ExecuteScalarAsync<long>(sql, client);
    }
}

public class ApiKeyRepository : IApiKeyRepository
{
    private readonly IDbConnectionFactory _connectionFactory;

    public ApiKeyRepository(IDbConnectionFactory connectionFactory)
    {
        _connectionFactory = connectionFactory;
    }

    public async Task<ApiKey?> GetByHashAsync(string appId, string secretHash)
    {
        using var connection = _connectionFactory.CreateConnection();
        var sql = @"SELECT k.id AS Id, k.client_id AS ClientId, k.key_prefix AS KeyPrefix, k.expires_at AS ExpiresAt, k.is_revoked AS IsRevoked 
                    FROM tblApiKeys k 
                    JOIN tblApiClients c ON k.client_id = c.id 
                    WHERE c.app_id = @appId AND k.secret_hash = @secretHash AND k.void = 0";
        return await connection.QueryFirstOrDefaultAsync<ApiKey>(sql, new { appId, secretHash });
    }

    public async Task<IEnumerable<ApiKey>> GetByClientIdAsync(long clientId)
    {
        using var connection = _connectionFactory.CreateConnection();
        var sql = "SELECT id AS Id, key_prefix AS KeyPrefix, expires_at AS ExpiresAt, is_revoked AS IsRevoked FROM tblApiKeys WHERE client_id = @clientId AND void = 0";
        return await connection.QueryAsync<ApiKey>(sql, new { clientId });
    }

    public async Task<long> CreateAsync(ApiKey apiKey)
    {
        using var connection = _connectionFactory.CreateConnection();
        var sql = @"INSERT INTO tblApiKeys (internal_id, client_id, key_prefix, secret_hash, expires_at, is_revoked, created_at, void) 
                    VALUES (NEWID(), @ClientId, @KeyPrefix, @SecretHash, @ExpiresAt, 0, GETUTCDATE(), 0);
                    SELECT CAST(SCOPE_IDENTITY() as BIGINT);";
        return await connection.ExecuteScalarAsync<long>(sql, apiKey);
    }

    public async Task RevokeAsync(long id)
    {
        using var connection = _connectionFactory.CreateConnection();
        await connection.ExecuteAsync("UPDATE tblApiKeys SET is_revoked = 1, updated_at = GETUTCDATE() WHERE id = @id", new { id });
    }

    public async Task DeleteExpiredAsync()
    {
        using var connection = _connectionFactory.CreateConnection();
        await connection.ExecuteAsync("UPDATE tblApiKeys SET void = 1 WHERE expires_at < GETUTCDATE() AND void = 0");
    }
}
