using WarrantyBee.Shared.Infrastructure.Abstractions;
using System.Data;
using Dapper;
using WarrantyBee.Application.Abstractions.Persistence;

namespace WarrantyBee.API.Infrastructure.Persistence;

public class RefreshTokenRepository : IRefreshTokenRepository
{
    private readonly IDbConnectionFactory _connectionFactory;

    public RefreshTokenRepository(IDbConnectionFactory connectionFactory)
    {
        _connectionFactory = connectionFactory;
    }

    public async Task AddAsync(long userId, string tokenHash, DateTime expiresAt)
    {
        using var connection = _connectionFactory.CreateConnection();
        var sql = "INSERT INTO tblRefreshTokens (user_id, token_hash, expires_at, is_revoked, created_at) VALUES (@userId, @tokenHash, @expiresAt, 0, GETUTCDATE())";
        await connection.ExecuteAsync(sql, new { userId, tokenHash, expiresAt });
    }

    public async Task<RefreshTokenRecord?> GetByHashAsync(string tokenHash)
    {
        using var connection = _connectionFactory.CreateConnection();
        var sql = "SELECT id AS Id, user_id AS UserId, token_hash AS TokenHash, expires_at AS ExpiresAt, is_revoked AS IsRevoked, replaced_by_token_hash AS ReplacedByTokenHash FROM tblRefreshTokens WHERE token_hash = @tokenHash";
        return await connection.QueryFirstOrDefaultAsync<RefreshTokenRecord>(sql, new { tokenHash });
    }

    public async Task RevokeAsync(long id, string replacedByTokenHash = null)
    {
        using var connection = _connectionFactory.CreateConnection();
        var sql = "UPDATE tblRefreshTokens SET is_revoked = 1, replaced_by_token_hash = @replacedByTokenHash WHERE id = @id";
        await connection.ExecuteAsync(sql, new { id, replacedByTokenHash });
    }

    public async Task RevokeDescendantsAsync(string tokenHash)
    {
        using var connection = _connectionFactory.CreateConnection();
        // Recursive common table expression to find and revoke all descendants in a token family
        var sql = @";WITH Descendants AS (
                        SELECT id FROM tblRefreshTokens WHERE token_hash = @tokenHash
                        UNION ALL
                        SELECT t.id FROM tblRefreshTokens t
                        INNER JOIN Descendants d ON t.token_hash = (SELECT replaced_by_token_hash FROM tblRefreshTokens WHERE id = d.id)
                    )
                    UPDATE tblRefreshTokens SET is_revoked = 1 WHERE id IN (SELECT id FROM Descendants)";
        
        // Simplified non-recursive for initial version (database usually doesn't need complex CTE for 1-level rotation)
        var sqlSimple = "UPDATE tblRefreshTokens SET is_revoked = 1 WHERE user_id = (SELECT user_id FROM tblRefreshTokens WHERE token_hash = @tokenHash)";
        await connection.ExecuteAsync(sqlSimple, new { tokenHash });
    }
}
