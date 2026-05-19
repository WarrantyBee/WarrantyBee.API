using WarrantyBee.Application.Contracts.Common;

namespace WarrantyBee.Application.Contracts.Identity;

/// <summary>
/// Represents a request to sign up a new user.
/// </summary>
public class SignUpRequest : BaseRequest
{
    /// <summary>
    /// Gets or sets the user's first name.
    /// </summary>
    public string Firstname { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the user's last name.
    /// </summary>
    public string Lastname { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the user's email address.
    /// </summary>
    public string Email { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the user's password.
    /// </summary>
    public string Password { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets a value indicating whether the user has accepted the terms and conditions.
    /// </summary>
    public bool HasAcceptedTermsAndConditions { get; set; }

    /// <summary>
    /// Gets or sets a value indicating whether the user has accepted the privacy policy.
    /// </summary>
    public bool HasAcceptedPrivacyPolicy { get; set; }

    /// <summary>
    /// Gets or sets the first line of the user's address.
    /// </summary>
    public string? AddressLine1 { get; set; }

    /// <summary>
    /// Gets or sets the second line of the user's address.
    /// </summary>
    public string? AddressLine2 { get; set; }

    /// <summary>
    /// Gets or sets the user's date of birth.
    /// </summary>
    public DateTime? DateOfBirth { get; set; }

    /// <summary>
    /// Gets or sets the phone country code.
    /// </summary>
    public string? PhoneCode { get; set; }

    /// <summary>
    /// Gets or sets the user's phone number.
    /// </summary>
    public string? PhoneNumber { get; set; }

    /// <summary>
    /// Gets or sets the user's gender.
    /// </summary>
    public byte? Gender { get; set; }

    /// <summary>
    /// Gets or sets the region identifier.
    /// </summary>
    public long? RegionId { get; set; }

    /// <summary>
    /// Gets or sets the country identifier.
    /// </summary>
    public long? CountryId { get; set; }

    /// <summary>
    /// Gets or sets the city.
    /// </summary>
    public string? City { get; set; }

    /// <summary>
    /// Gets or sets the postal code.
    /// </summary>
    public string? PostalCode { get; set; }

    /// <summary>
    /// Gets or sets the URL of the user's avatar.
    /// </summary>
    public string? AvatarUrl { get; set; }

    /// <summary>
    /// Gets or sets the culture identifier.
    /// </summary>
    public long? CultureId { get; set; }

    /// <summary>
    /// Gets or sets the authentication provider.
    /// </summary>
    public byte? AuthProvider { get; set; }

    /// <summary>
    /// Gets or sets the identifier provided by the authentication provider.
    /// </summary>
    public string? AuthProviderUserId { get; set; }
}
