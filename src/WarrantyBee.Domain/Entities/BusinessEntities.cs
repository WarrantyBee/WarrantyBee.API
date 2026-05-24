namespace WarrantyBee.Domain.Entities;

public class BusinessProfile
{
    public long Id { get; set; }
    public Guid InternalId { get; set; }
    public string Name { get; set; } = string.Empty;
    public string? LegalName { get; set; }
    public string? TaxId { get; set; }
    public string? Website { get; set; }
    public string? LogoUrl { get; set; }
    public string? SupportEmail { get; set; }
    public bool IsVerified { get; set; }
    public long OwnerUserId { get; set; }
    public DateTime CreatedAt { get; set; }
    public DateTime? UpdatedAt { get; set; }
}

public class OnboardingLink
{
    public long Id { get; set; }
    public Guid Token { get; set; }
    public string Email { get; set; } = string.Empty;
    public long TargetRoleId { get; set; }
    public long? TargetBusinessId { get; set; }
    public long InviterUserId { get; set; }
    public DateTime ExpiresAt { get; set; }
    public bool IsUsed { get; set; }
    public DateTime CreatedAt { get; set; }
}
