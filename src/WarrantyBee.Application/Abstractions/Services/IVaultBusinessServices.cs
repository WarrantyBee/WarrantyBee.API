using WarrantyBee.Domain.Entities;

namespace WarrantyBee.Application.Abstractions.Services;

public interface IVaultService
{
    Task<IEnumerable<UserAppliance>> GetMyVaultAsync();
    Task<UserAppliance> RegisterApplianceAsync(RegisterApplianceRequest request);
}

public interface IClaimService
{
    Task<IEnumerable<Claim>> GetMyClaimsAsync();
    Task<IEnumerable<Claim>> GetBusinessClaimsAsync();
    Task<Claim> FileClaimAsync(FileClaimRequest request);
    Task<Claim> GetClaimDetailsAsync(long id);
    Task UpdateClaimStatusAsync(long id, string status, string? notes);
    
    Task AddMessageAsync(long claimId, string message, string? attachmentUrl);
    Task<IEnumerable<ClaimMessage>> GetMessagesAsync(long claimId);
}

public interface IProductCatalogService
{
    Task<IEnumerable<Product>> GetMyProductsAsync();
    Task<Product> AddProductAsync(AddProductRequest request);
}

public class RegisterApplianceRequest
{
    public string Sku { get; set; } = string.Empty;
    public string SerialNumber { get; set; } = string.Empty;
    public DateTime PurchaseDate { get; set; }
    public string? ReceiptUrl { get; set; }
}

public class FileClaimRequest
{
    public long UserApplianceId { get; set; }
    public string IssueCategory { get; set; } = string.Empty;
    public string IssueDescription { get; set; } = string.Empty;
    public string? DefectMediaUrl { get; set; }
}

public class AddProductRequest
{
    public string Sku { get; set; } = string.Empty;
    public string Name { get; set; } = string.Empty;
    public string Category { get; set; } = string.Empty;
    public string? ImageUrl { get; set; }
    public int DefaultWarrantyMonths { get; set; } = 12;
}
