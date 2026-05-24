using WarrantyBee.Shared.Infrastructure.Abstractions;
using System.Data;
using Dapper;
using WarrantyBee.Application.Abstractions.Persistence;
using WarrantyBee.Domain.Entities;

namespace WarrantyBee.API.Infrastructure.Persistence;

public class BusinessRepository : IBusinessRepository
{
    private readonly IDbConnectionFactory _connectionFactory;

    public BusinessRepository(IDbConnectionFactory connectionFactory)
    {
        _connectionFactory = connectionFactory;
    }

    public async Task<BusinessProfile?> GetByIdAsync(long id)
    {
        using var connection = _connectionFactory.CreateConnection();
        return await connection.QueryFirstOrDefaultAsync<BusinessProfile>(
            "SELECT id AS Id, internal_id AS InternalId, name AS Name, legal_name AS LegalName, tax_id AS TaxId, website AS Website, logo_url AS LogoUrl, support_email AS SupportEmail, is_verified AS IsVerified, owner_user_id AS OwnerUserId FROM tblBusinessProfiles WHERE id = @id AND void = 0", new { id });
    }

    public async Task<BusinessProfile?> GetByOwnerIdAsync(long ownerUserId)
    {
        using var connection = _connectionFactory.CreateConnection();
        return await connection.QueryFirstOrDefaultAsync<BusinessProfile>(
            "SELECT id AS Id, internal_id AS InternalId, name AS Name, legal_name AS LegalName, tax_id AS TaxId, website AS Website, logo_url AS LogoUrl, support_email AS SupportEmail, is_verified AS IsVerified, owner_user_id AS OwnerUserId FROM tblBusinessProfiles WHERE owner_user_id = @ownerUserId AND void = 0", new { ownerUserId });
    }

    public async Task<IEnumerable<BusinessProfile>> GetPendingVerificationAsync()
    {
        using var connection = _connectionFactory.CreateConnection();
        return await connection.QueryAsync<BusinessProfile>(
            "SELECT id AS Id, internal_id AS InternalId, name AS Name, legal_name AS LegalName, tax_id AS TaxId, website AS Website, logo_url AS LogoUrl, support_email AS SupportEmail, is_verified AS IsVerified, owner_user_id AS OwnerUserId FROM tblBusinessProfiles WHERE is_verified = 0 AND void = 0");
    }

    public async Task<long> CreateAsync(BusinessProfile profile)
    {
        using var connection = _connectionFactory.CreateConnection();
        var sql = @"INSERT INTO tblBusinessProfiles (internal_id, name, legal_name, tax_id, website, logo_url, support_email, is_verified, owner_user_id, created_at, void) 
                    VALUES (NEWID(), @Name, @LegalName, @TaxId, @Website, @LogoUrl, @SupportEmail, 0, @OwnerUserId, GETUTCDATE(), 0);
                    SELECT CAST(SCOPE_IDENTITY() as BIGINT);";
        return await connection.ExecuteScalarAsync<long>(sql, profile);
    }

    public async Task UpdateAsync(BusinessProfile profile)
    {
        using var connection = _connectionFactory.CreateConnection();
        var sql = "UPDATE tblBusinessProfiles SET name = @Name, legal_name = @LegalName, tax_id = @TaxId, website = @Website, logo_url = @LogoUrl, support_email = @SupportEmail, updated_at = GETUTCDATE() WHERE id = @Id";
        await connection.ExecuteAsync(sql, profile);
    }

    public async Task VerifyAsync(long id)
    {
        using var connection = _connectionFactory.CreateConnection();
        await connection.ExecuteAsync("UPDATE tblBusinessProfiles SET is_verified = 1, updated_at = GETUTCDATE() WHERE id = @id", new { id });
    }
}

public class OnboardingRepository : IOnboardingRepository
{
    private readonly IDbConnectionFactory _connectionFactory;

    public OnboardingRepository(IDbConnectionFactory connectionFactory)
    {
        _connectionFactory = connectionFactory;
    }

    public async Task AddLinkAsync(OnboardingLink link)
    {
        using var connection = _connectionFactory.CreateConnection();
        var sql = @"INSERT INTO tblOnboardingLinks (token, email, target_role_id, target_business_id, inviter_user_id, expires_at, is_used, created_at) 
                    VALUES (@Token, @Email, @TargetRoleId, @TargetBusinessId, @InviterUserId, @ExpiresAt, 0, GETUTCDATE())";
        await connection.ExecuteAsync(sql, link);
    }

    public async Task<OnboardingLink?> GetByTokenAsync(Guid token)
    {
        using var connection = _connectionFactory.CreateConnection();
        return await connection.QueryFirstOrDefaultAsync<OnboardingLink>(
            "SELECT id AS Id, token AS Token, email AS Email, target_role_id AS TargetRoleId, target_business_id AS TargetBusinessId, inviter_user_id AS InviterUserId, expires_at AS ExpiresAt, is_used AS IsUsed FROM tblOnboardingLinks WHERE token = @token", new { token });
    }

    public async Task MarkAsUsedAsync(Guid token)
    {
        using var connection = _connectionFactory.CreateConnection();
        await connection.ExecuteAsync("UPDATE tblOnboardingLinks SET is_used = 1 WHERE token = @token", new { token });
    }
}
