namespace WarrantyBee.Domain.Entities;

public class Product
{
    public long Id { get; set; }
    public long BusinessId { get; set; }
    public string Sku { get; set; } = string.Empty;
    public string Name { get; set; } = string.Empty;
    public string Category { get; set; } = string.Empty;
    public string? ImageUrl { get; set; }
    public int DefaultWarrantyMonths { get; set; }
}

public class UserAppliance
{
    public long Id { get; set; }
    public long UserId { get; set; }
    public long ProductId { get; set; }
    public string SerialNumber { get; set; } = string.Empty;
    public DateTime PurchaseDate { get; set; }
    public DateTime WarrantyEndDate { get; set; }
    public string? ReceiptUrl { get; set; }
    public string Status { get; set; } = "ACTIVE";
    
    // Extended properties for UI
    public string? ProductName { get; set; }
    public string? ProductCategory { get; set; }
    public string? ProductImageUrl { get; set; }
}

public class Claim
{
    public long Id { get; set; }
    public string ClaimNumber { get; set; } = string.Empty;
    public long UserApplianceId { get; set; }
    public long CustomerId { get; set; }
    public long BusinessId { get; set; }
    public string IssueCategory { get; set; } = string.Empty;
    public string IssueDescription { get; set; } = string.Empty;
    public string? DefectMediaUrl { get; set; }
    public string Status { get; set; } = "SUBMITTED";
    public string? ResolutionNotes { get; set; }
    public DateTime CreatedAt { get; set; }
    
    // Joined info
    public string? ProductName { get; set; }
    public string? CustomerName { get; set; }
}

public class ClaimMessage
{
    public long Id { get; set; }
    public long ClaimId { get; set; }
    public long SenderUserId { get; set; }
    public string Message { get; set; } = string.Empty;
    public string? AttachmentUrl { get; set; }
    public DateTime CreatedAt { get; set; }
}
