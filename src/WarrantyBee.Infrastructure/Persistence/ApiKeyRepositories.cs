using WarrantyBee.Shared.Infrastructure.Abstractions;
using System.Data;
using Dapper;
using WarrantyBee.Application.Abstractions.Persistence;
using WarrantyBee.Domain.Entities;
using WarrantyBee.Shared.Security.Abstractions;
using WarrantyBee.Shared.Core.Enums;

namespace WarrantyBee.API.Infrastructure.Persistence;

public class ApiClientRepository : WarrantyBee.Application.Abstractions.Persistence.IApiClientRepository
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
            "SELECT id AS Id, app_id AS AppId, name AS Name, description AS Description, app_secret AS AppSecret, owner_user_id AS OwnerUserId, app_type AS AppType FROM tblApiClients WHERE app_id = @appId AND void = 0", 
            new { appId });
    }

    public async Task<IEnumerable<ApiClient>> GetAllAsync()
    {
        using var connection = _connectionFactory.CreateConnection();
        return await connection.QueryAsync<ApiClient>(
            "SELECT id AS Id, app_id AS AppId, name AS Name, description AS Description, app_secret AS AppSecret, owner_user_id AS OwnerUserId, app_type AS AppType FROM tblApiClients WHERE void = 0");
    }

    public async Task<long> CreateAsync(ApiClient client)
    {
        using var connection = _connectionFactory.CreateConnection();
        var sql = @"INSERT INTO tblApiClients (internal_id, app_id, name, description, app_secret, owner_user_id, app_type, created_at, void) 
                    VALUES (NEWID(), @AppId, @Name, @Description, @AppSecret, @OwnerUserId, @AppType, GETUTCDATE(), 0);
                    SELECT CAST(SCOPE_IDENTITY() as BIGINT);";
        return await connection.ExecuteScalarAsync<long>(sql, client);
    }
}

public class ApiKeyRepository : WarrantyBee.Shared.Security.Abstractions.IApiKeyRepository, WarrantyBee.Application.Abstractions.Persistence.IApiKeyRepository
{
    private readonly IDbConnectionFactory _connectionFactory;

    public ApiKeyRepository(IDbConnectionFactory connectionFactory)
    {
        _connectionFactory = connectionFactory;
    }

    // Shared Security Implementation
    public async Task<ApiClientContext?> ResolveAsync(string appId, string secretHash, string requestedPath)
    {
        using var connection = _connectionFactory.CreateConnection();
        var sql = @"SELECT c.id AS ClientId, c.app_id AS AppId, c.name AS Name, r.name AS RoleName, 
                    STUFF((SELECT ',' + p.name FROM tblRolePermissions rp JOIN tblPermissions p ON rp.permission_id = p.id WHERE rp.role_id = c.role_id FOR XML PATH('')), 1, 1, '') AS PermissionsCsv
                    FROM tblApiKeys k 
                    JOIN tblApiClients c ON k.client_id = c.id 
                    JOIN tblRoles r ON c.role_id = r.id
                    LEFT JOIN tblApiKeyEndpoints e ON k.id = e.api_key_id
                    WHERE c.app_id = @appId 
                    AND k.secret_hash = @secretHash 
                    AND k.is_revoked = 0 
                    AND k.expires_at > GETUTCDATE() 
                    AND k.void = 0
                    AND (e.endpoint_path IS NULL OR @requestedPath LIKE e.endpoint_path + '%')";
        
        var result = await connection.QueryFirstOrDefaultAsync<dynamic>(sql, new { appId, secretHash, requestedPath });
        if (result == null) return null;

        return MapToContext(result);
    }

    public async Task<ApiClientContext?> ResolveKeyAsync(string keyHash, string requestedPath)
    {
        using var connection = _connectionFactory.CreateConnection();
        var sql = @"SELECT c.id AS ClientId, c.app_id AS AppId, c.name AS Name, r.name AS RoleName,
                    STUFF((SELECT ',' + p.name FROM tblRolePermissions rp JOIN tblPermissions p ON rp.permission_id = p.id WHERE rp.role_id = c.role_id FOR XML PATH('')), 1, 1, '') AS PermissionsCsv
                    FROM tblApiKeys k 
                    JOIN tblApiClients c ON k.client_id = c.id
                    JOIN tblRoles r ON c.role_id = r.id
                    LEFT JOIN tblApiKeyEndpoints e ON k.id = e.api_key_id
                    WHERE k.secret_hash = @keyHash 
                    AND k.is_revoked = 0 
                    AND k.expires_at > GETUTCDATE() 
                    AND k.void = 0
                    AND (e.endpoint_path IS NULL OR @requestedPath LIKE e.endpoint_path + '%')";

        var result = await connection.QueryFirstOrDefaultAsync<dynamic>(sql, new { keyHash, requestedPath });
        if (result == null) return null;

        return MapToContext(result);
    }

    // Existing Management Implementation
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

    public async Task UpdateEndpointsAsync(long keyId, IEnumerable<string> endpoints)
    {
        using var connection = _connectionFactory.CreateConnection();
        connection.Open();
        using var transaction = connection.BeginTransaction();
        try
        {
            await connection.ExecuteAsync("DELETE FROM tblApiKeyEndpoints WHERE api_key_id = @keyId", new { keyId }, transaction);
            foreach (var path in endpoints)
            {
                await connection.ExecuteAsync("INSERT INTO tblApiKeyEndpoints (api_key_id, endpoint_path, created_at) VALUES (@keyId, @path, GETUTCDATE())", new { keyId, path }, transaction);
            }
            transaction.Commit();
        }
        catch
        {
            transaction.Rollback();
            throw;
        }
    }

    private ApiClientContext MapToContext(dynamic row)
    {
        var roleName = (string)row.RoleName;
        var permissionsCsv = (string)row.PermissionsCsv ?? "";
        
        return new ApiClientContext
        {
            ClientId = (long)row.ClientId,
            AppId = (string)row.AppId,
            Name = (string)row.Name,
            Role = Enum.TryParse<SecurityRole>(roleName, true, out var role) ? role : SecurityRole.None,
            Permissions = permissionsCsv.Split(',', StringSplitOptions.RemoveEmptyEntries)
                .Select(p => Enum.TryParse<SecurityPermission>(p, true, out var perm) ? perm : SecurityPermission.None)
                .Where(p => p != SecurityPermission.None)
        };
    }
}
