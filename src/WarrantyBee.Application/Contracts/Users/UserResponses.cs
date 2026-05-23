using WarrantyBee.Application.Contracts.Geographic;

namespace WarrantyBee.Application.Contracts.Users;

/// <summary>
/// Represents the settings for a user.
/// </summary>
public class UserSettingsResponse
{
    /// <summary>
    /// Gets or sets a value indicating whether two-factor authentication is enabled.
    /// </summary>
    public bool Is2FAEnabled { get; set; }

    /// <summary>
    /// Gets or sets the date and time when the password was last updated.
    /// </summary>
    public DateTime? PasswordUpdatedAt { get; set; }
}

/// <summary>
/// Represents the address information of a user.
/// </summary>
public class UserAddressResponse
{
    /// <summary>
    /// Gets or sets the first line of the address.
    /// </summary>
    public string? AddressLine1 { get; set; }

    /// <summary>
    /// Gets or sets the second line of the address.
    /// </summary>
    public string? AddressLine2 { get; set; }

    /// <summary>
    /// Gets or sets the city.
    /// </summary>
    public string? City { get; set; }

    /// <summary>
    /// Gets or sets the postal code.
    /// </summary>
    public string? PostalCode { get; set; }

    /// <summary>
    /// Gets or sets the region information.
    /// </summary>
    public RegionResponse? Region { get; set; }

    /// <summary>
    /// Gets or sets the country information.
    /// </summary>
    public CountryResponse? Country { get; set; }
}

/// <summary>
/// Represents the profile information of a user.
/// </summary>
public class UserProfileResponse
{
    /// <summary>
    /// Gets or sets the phone country code.
    /// </summary>
    public string? PhoneCode { get; set; }

    /// <summary>
    /// Gets or sets the phone number.
    /// </summary>
    public string? PhoneNumber { get; set; }

    /// <summary>
    /// Gets or sets the gender of the user.
    /// </summary>
    public byte? Gender { get; set; }

    /// <summary>
    /// Gets or sets the date of birth.
    /// </summary>
    public DateTime? DateOfBirth { get; set; }

    /// <summary>
    /// Gets or sets the address information.
    /// </summary>
    public UserAddressResponse? Address { get; set; }

    /// <summary>
    /// Gets or sets the timezone information.
    /// </summary>
    public TimeZoneResponse? Timezone { get; set; }

    /// <summary>
    /// Gets or sets the currency information.
    /// </summary>
    public CurrencyResponse? Currency { get; set; }

    /// <summary>
    /// Gets or sets the culture information.
    /// </summary>
    public CultureResponse? Culture { get; set; }

    /// <summary>
    /// Gets or sets the user settings.
    /// </summary>
    public UserSettingsResponse? Settings { get; set; }

    /// <summary>
    /// Gets or sets the URL of the user's avatar.
    /// </summary>
    public string? AvatarUrl { get; set; }
}

/// <summary>
/// Represents a user in the system.
/// </summary>
public class UserResponse
{
    /// <summary>
    /// Gets or sets the user identifier.
    /// </summary>
    public long Id { get; set; }

    /// <summary>
    /// Gets or sets the user's first name.
    /// </summary>
    public string? Firstname { get; set; }

    /// <summary>
    /// Gets or sets the user's last name.
    /// </summary>
    public string? Lastname { get; set; }

    /// <summary>
    /// Gets or sets the user's email address.
    /// </summary>
    public string? Email { get; set; }

    /// <summary>
    /// Gets or sets the user's profile information.
    /// </summary>
    public UserProfileResponse? Profile { get; set; }
    
    /// <summary>
    /// Gets or sets the hashed password. This field is for internal use.
    /// </summary>
    public string? Password { get; set; }

    /// <summary>
    /// Gets or sets the authentication provider.
    /// </summary>
    public byte? AuthProvider { get; set; }

    /// <summary>
    /// Gets or sets the identifier provided by the authentication provider.
    /// </summary>
    public string? AuthProviderUserId { get; set; }

    /// <summary>
    /// Gets or sets the authorization context, including roles and permissions.
    /// </summary>
    public UserAuthorization? AuthorizationContext { get; set; }
}

/// <summary>
/// Represents the authorization information for a user.
/// </summary>
public class UserAuthorization
{
    /// <summary>
    /// Gets or sets the security role of the user.
    /// </summary>
    public WarrantyBee.Shared.Core.Enums.SecurityRole Role { get; set; }

    /// <summary>
    /// Gets or sets the collection of security permissions assigned to the user.
    /// </summary>
    public IEnumerable<WarrantyBee.Shared.Core.Enums.SecurityPermission> Permissions { get; set; } = [];
}

