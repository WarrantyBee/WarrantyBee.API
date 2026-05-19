using WarrantyBee.Application.Contracts.Common;

namespace WarrantyBee.Application.Contracts.Identity;

public class SignUpRequest : BaseRequest
{
    public string Firstname { get; set; } = string.Empty;
    public string Lastname { get; set; } = string.Empty;
    public string Email { get; set; } = string.Empty;
    public string Password { get; set; } = string.Empty;
    public bool HasAcceptedTermsAndConditions { get; set; }
    public bool HasAcceptedPrivacyPolicy { get; set; }
    public string? AddressLine1 { get; set; }
    public string? AddressLine2 { get; set; }
    public DateTime? DateOfBirth { get; set; }
    public string? PhoneCode { get; set; }
    public string? PhoneNumber { get; set; }
    public byte? Gender { get; set; }
    public long? RegionId { get; set; }
    public long? CountryId { get; set; }
    public string? City { get; set; }
    public string? PostalCode { get; set; }
    public string? AvatarUrl { get; set; }
    public long? CultureId { get; set; }
    public byte? AuthProvider { get; set; }
    public string? AuthProviderUserId { get; set; }
}
