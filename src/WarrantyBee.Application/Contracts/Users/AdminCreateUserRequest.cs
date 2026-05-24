namespace WarrantyBee.Application.Contracts.Users;

/// <summary>
/// Represents a request for an administrator to create a new user with a specific role.
/// </summary>
public class AdminCreateUserRequest
{
    public string Firstname { get; set; } = string.Empty;
    public string Lastname { get; set; } = string.Empty;
    public string Email { get; set; } = string.Empty;
    public string Password { get; set; } = string.Empty;
    public long RoleId { get; set; }
    public string? PhoneCode { get; set; }
    public string? PhoneNumber { get; set; }
    public byte? Gender { get; set; }
    public long? CountryId { get; set; }
    public long? RegionId { get; set; }
    public string? City { get; set; }
    public string? PostalCode { get; set; }
    public long? CultureId { get; set; }
}
