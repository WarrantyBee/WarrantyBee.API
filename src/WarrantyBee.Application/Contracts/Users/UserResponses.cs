using WarrantyBee.Application.Contracts.Geographic;

namespace WarrantyBee.Application.Contracts.Users;

public class UserSettingsResponse
{
    public bool Is2FAEnabled { get; set; }
    public DateTime? PasswordUpdatedAt { get; set; }
}

public class UserAddressResponse
{
    public string? AddressLine1 { get; set; }
    public string? AddressLine2 { get; set; }
    public string? City { get; set; }
    public string? PostalCode { get; set; }
    public RegionResponse? Region { get; set; }
    public CountryResponse? Country { get; set; }
}

public class UserProfileResponse
{
    public string? PhoneCode { get; set; }
    public string? PhoneNumber { get; set; }
    public byte? Gender { get; set; }
    public DateTime? DateOfBirth { get; set; }
    public UserAddressResponse? Address { get; set; }
    public TimeZoneResponse? Timezone { get; set; }
    public CurrencyResponse? Currency { get; set; }
    public CultureResponse? Culture { get; set; }
    public UserSettingsResponse? Settings { get; set; }
    public string? AvatarUrl { get; set; }
}

public class UserResponse
{
    public long Id { get; set; }
    public string? Firstname { get; set; }
    public string? Lastname { get; set; }
    public string? Email { get; set; }
    public UserProfileResponse? Profile { get; set; }
    
    // Internal/Hidden fields for Auth logic
    public string? Password { get; set; }
    public byte? AuthProvider { get; set; }
    public string? AuthProviderUserId { get; set; }
    public UserAuthorization? AuthorizationContext { get; set; }
}

public class UserAuthorization
{
    public Domain.Enums.SecurityRole Role { get; set; }
    public IEnumerable<Domain.Enums.SecurityPermission> Permissions { get; set; } = [];
}
