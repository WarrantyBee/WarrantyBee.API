using WarrantyBee.Shared.Infrastructure.Abstractions;
using System.Data;
using Dapper;
using WarrantyBee.Application.Abstractions.Persistence;
using WarrantyBee.Domain.Entities;

namespace WarrantyBee.API.Infrastructure.Persistence;

public class ProductRepository : IProductRepository
{
    private readonly IDbConnectionFactory _connectionFactory;

    public ProductRepository(IDbConnectionFactory connectionFactory)
    {
        _connectionFactory = connectionFactory;
    }

    public async Task<Product?> GetByIdAsync(long id)
    {
        using var connection = _connectionFactory.CreateConnection();
        return await connection.QueryFirstOrDefaultAsync<Product>(
            "SELECT id AS Id, business_id AS BusinessId, sku AS Sku, name AS Name, category AS Category, image_url AS ImageUrl, default_warranty_months AS DefaultWarrantyMonths FROM tblProducts WHERE id = @id AND void = 0", new { id });
    }

    public async Task<Product?> GetBySkuAsync(string sku)
    {
        using var connection = _connectionFactory.CreateConnection();
        return await connection.QueryFirstOrDefaultAsync<Product>(
            "SELECT id AS Id, business_id AS BusinessId, sku AS Sku, name AS Name, category AS Category, image_url AS ImageUrl, default_warranty_months AS DefaultWarrantyMonths FROM tblProducts WHERE sku = @sku AND void = 0", new { sku });
    }

    public async Task<IEnumerable<Product>> GetByBusinessIdAsync(long businessId)
    {
        using var connection = _connectionFactory.CreateConnection();
        return await connection.QueryAsync<Product>(
            "SELECT id AS Id, business_id AS BusinessId, sku AS Sku, name AS Name, category AS Category, image_url AS ImageUrl, default_warranty_months AS DefaultWarrantyMonths FROM tblProducts WHERE business_id = @businessId AND void = 0", new { businessId });
    }

    public async Task<long> CreateAsync(Product product)
    {
        using var connection = _connectionFactory.CreateConnection();
        var sql = @"INSERT INTO tblProducts (business_id, sku, name, category, image_url, default_warranty_months, created_at, void) 
                    VALUES (@BusinessId, @Sku, @Name, @Category, @ImageUrl, @DefaultWarrantyMonths, GETUTCDATE(), 0);
                    SELECT CAST(SCOPE_IDENTITY() as BIGINT);";
        return await connection.ExecuteScalarAsync<long>(sql, product);
    }

    public async Task UpdateAsync(Product product)
    {
        using var connection = _connectionFactory.CreateConnection();
        var sql = "UPDATE tblProducts SET name = @Name, category = @Category, image_url = @ImageUrl, default_warranty_months = @DefaultWarrantyMonths, updated_at = GETUTCDATE() WHERE id = @Id";
        await connection.ExecuteAsync(sql, product);
    }
}

public class VaultRepository : IVaultRepository
{
    private readonly IDbConnectionFactory _connectionFactory;

    public VaultRepository(IDbConnectionFactory connectionFactory)
    {
        _connectionFactory = connectionFactory;
    }

    public async Task<UserAppliance?> GetByIdAsync(long id)
    {
        using var connection = _connectionFactory.CreateConnection();
        var sql = @"SELECT v.id AS Id, v.user_id AS UserId, v.product_id AS ProductId, v.serial_number AS SerialNumber, 
                           v.purchase_date AS PurchaseDate, v.warranty_end_date AS WarrantyEndDate, v.receipt_url AS ReceiptUrl, v.status AS Status,
                           p.name AS ProductName, p.category AS ProductCategory, p.image_url AS ProductImageUrl
                    FROM tblUserAppliances v
                    JOIN tblProducts p ON v.product_id = p.id
                    WHERE v.id = @id AND v.void = 0";
        return await connection.QueryFirstOrDefaultAsync<UserAppliance>(sql, new { id });
    }

    public async Task<IEnumerable<UserAppliance>> GetByUserIdAsync(long userId)
    {
        using var connection = _connectionFactory.CreateConnection();
        var sql = @"SELECT v.id AS Id, v.user_id AS UserId, v.product_id AS ProductId, v.serial_number AS SerialNumber, 
                           v.purchase_date AS PurchaseDate, v.warranty_end_date AS WarrantyEndDate, v.receipt_url AS ReceiptUrl, v.status AS Status,
                           p.name AS ProductName, p.category AS ProductCategory, p.image_url AS ProductImageUrl
                    FROM tblUserAppliances v
                    JOIN tblProducts p ON v.product_id = p.id
                    WHERE v.user_id = @userId AND v.void = 0";
        return await connection.QueryAsync<UserAppliance>(sql, new { userId });
    }

    public async Task<long> CreateAsync(UserAppliance appliance)
    {
        using var connection = _connectionFactory.CreateConnection();
        var sql = @"INSERT INTO tblUserAppliances (user_id, product_id, serial_number, purchase_date, warranty_end_date, receipt_url, status, created_at, void) 
                    VALUES (@UserId, @ProductId, @SerialNumber, @PurchaseDate, @WarrantyEndDate, @ReceiptUrl, 'ACTIVE', GETUTCDATE(), 0);
                    SELECT CAST(SCOPE_IDENTITY() as BIGINT);";
        return await connection.ExecuteScalarAsync<long>(sql, appliance);
    }

    public async Task UpdateStatusAsync(long id, string status)
    {
        using var connection = _connectionFactory.CreateConnection();
        await connection.ExecuteAsync("UPDATE tblUserAppliances SET status = @status, updated_at = GETUTCDATE() WHERE id = @id", new { id, status });
    }
}

public class ClaimRepository : IClaimRepository
{
    private readonly IDbConnectionFactory _connectionFactory;

    public ClaimRepository(IDbConnectionFactory connectionFactory)
    {
        _connectionFactory = connectionFactory;
    }

    public async Task<Claim?> GetByIdAsync(long id)
    {
        using var connection = _connectionFactory.CreateConnection();
        var sql = @"SELECT c.id AS Id, c.claim_number AS ClaimNumber, c.user_appliance_id AS UserApplianceId, 
                           c.customer_id AS CustomerId, c.business_id AS BusinessId, c.issue_category AS IssueCategory, 
                           c.issue_description AS IssueDescription, c.defect_media_url AS DefectMediaUrl, 
                           c.status AS Status, c.resolution_notes AS ResolutionNotes, c.created_at AS CreatedAt,
                           p.name AS ProductName, (u.firstname + ' ' + u.lastname) AS CustomerName
                    FROM tblClaims c
                    JOIN tblUserAppliances v ON c.user_appliance_id = v.id
                    JOIN tblProducts p ON v.product_id = p.id
                    JOIN tblUsers u ON c.customer_id = u.id
                    WHERE c.id = @id AND c.void = 0";
        return await connection.QueryFirstOrDefaultAsync<Claim>(sql, new { id });
    }

    public async Task<Claim?> GetByClaimNumberAsync(string claimNumber)
    {
        using var connection = _connectionFactory.CreateConnection();
        var sql = @"SELECT id AS Id, claim_number AS ClaimNumber FROM tblClaims WHERE claim_number = @claimNumber AND void = 0";
        return await connection.QueryFirstOrDefaultAsync<Claim>(sql, new { claimNumber });
    }

    public async Task<IEnumerable<Claim>> GetByCustomerIdAsync(long customerId)
    {
        using var connection = _connectionFactory.CreateConnection();
        var sql = @"SELECT c.id AS Id, c.claim_number AS ClaimNumber, c.status AS Status, c.created_at AS CreatedAt, p.name AS ProductName
                    FROM tblClaims c
                    JOIN tblUserAppliances v ON c.user_appliance_id = v.id
                    JOIN tblProducts p ON v.product_id = p.id
                    WHERE c.customer_id = @customerId AND c.void = 0";
        return await connection.QueryAsync<Claim>(sql, new { customerId });
    }

    public async Task<IEnumerable<Claim>> GetByBusinessIdAsync(long businessId)
    {
        using var connection = _connectionFactory.CreateConnection();
        var sql = @"SELECT c.id AS Id, c.claim_number AS ClaimNumber, c.status AS Status, c.created_at AS CreatedAt, 
                           p.name AS ProductName, (u.firstname + ' ' + u.lastname) AS CustomerName
                    FROM tblClaims c
                    JOIN tblUserAppliances v ON c.user_appliance_id = v.id
                    JOIN tblProducts p ON v.product_id = p.id
                    JOIN tblUsers u ON c.customer_id = u.id
                    WHERE c.business_id = @businessId AND c.void = 0";
        return await connection.QueryAsync<Claim>(sql, new { businessId });
    }

    public async Task<long> CreateAsync(Claim claim)
    {
        using var connection = _connectionFactory.CreateConnection();
        var sql = @"INSERT INTO tblClaims (claim_number, user_appliance_id, customer_id, business_id, issue_category, issue_description, defect_media_url, status, created_at, void) 
                    VALUES (@ClaimNumber, @UserApplianceId, @CustomerId, @BusinessId, @IssueCategory, @IssueDescription, @DefectMediaUrl, 'SUBMITTED', GETUTCDATE(), 0);
                    SELECT CAST(SCOPE_IDENTITY() as BIGINT);";
        return await connection.ExecuteScalarAsync<long>(sql, claim);
    }

    public async Task UpdateStatusAsync(long id, string status, string? resolutionNotes = null)
    {
        using var connection = _connectionFactory.CreateConnection();
        await connection.ExecuteAsync("UPDATE tblClaims SET status = @status, resolution_notes = @resolutionNotes, updated_at = GETUTCDATE() WHERE id = @id", new { id, status, resolutionNotes });
    }

    public async Task AddMessageAsync(ClaimMessage message)
    {
        using var connection = _connectionFactory.CreateConnection();
        var sql = "INSERT INTO tblClaimMessages (claim_id, sender_user_id, message, attachment_url, created_at) VALUES (@ClaimId, @SenderUserId, @Message, @AttachmentUrl, GETUTCDATE())";
        await connection.ExecuteAsync(sql, message);
    }

    public async Task<IEnumerable<ClaimMessage>> GetMessagesByClaimIdAsync(long claimId)
    {
        using var connection = _connectionFactory.CreateConnection();
        return await connection.QueryAsync<ClaimMessage>("SELECT id AS Id, claim_id AS ClaimId, sender_user_id AS SenderUserId, message AS Message, attachment_url AS AttachmentUrl, created_at AS CreatedAt FROM tblClaimMessages WHERE claim_id = @claimId ORDER BY created_at ASC", new { claimId });
    }
}
