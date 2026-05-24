using WarrantyBee.Application.Contracts.Identity;
using WarrantyBee.Application.Contracts.Users;
using WarrantyBee.Application.Contracts.Geographic;

namespace WarrantyBee.Application.Abstractions.Persistence;

/// <summary>
/// Represents a filter for searching for a user.
/// </summary>
public class UserSearchFilter
{
    /// <summary>
    /// Gets or sets the user's unique identifier.
    /// </summary>
    public long? Id { get; set; }

    /// <summary>
    /// Gets or sets the user's email address.
    /// </summary>
    public string? Email { get; set; }

    /// <summary>
    /// Initializes a new instance of the <see cref="UserSearchFilter"/> class.
    /// </summary>
    public UserSearchFilter() { }

    /// <summary>
    /// Initializes a new instance of the <see cref="UserSearchFilter"/> class with specified criteria.
    /// </summary>
    /// <param name="id">The user identifier.</param>
    /// <param name="email">The user email.</param>
    public UserSearchFilter(long? id, string? email)
    {
        Id = id;
        Email = email;
    }
}

/// <summary>
/// Represents details for a user's login token.
/// </summary>
public class LoginTokenDetails
{
    /// <summary>
    /// Gets or sets the user's unique identifier.
    /// </summary>
    public long UserId { get; set; }

    /// <summary>
    /// Gets or sets the login token.
    /// </summary>
    public string Token { get; set; } = string.Empty;

    /// <summary>
    /// Initializes a new instance of the <see cref="LoginTokenDetails"/> class.
    /// </summary>
    public LoginTokenDetails() { }

    /// <summary>
    /// Initializes a new instance of the <see cref="LoginTokenDetails"/> class with specified details.
    /// </summary>
    /// <param name="userId">The user identifier.</param>
    /// <param name="token">The login token.</param>
    public LoginTokenDetails(long userId, string token)
    {
        UserId = userId;
        Token = token;
    }
}

/// <summary>
/// Represents a request to reset a user's password.
/// </summary>
public class PasswordResetRequest
{
    /// <summary>
    /// Gets or sets the user's unique identifier.
    /// </summary>
    public long UserId { get; set; }

    /// <summary>
    /// Gets or sets the new password.
    /// </summary>
    public string NewPassword { get; set; } = string.Empty;

    /// <summary>
    /// Initializes a new instance of the <see cref="PasswordResetRequest"/> class.
    /// </summary>
    public PasswordResetRequest() { }

    /// <summary>
    /// Initializes a new instance of the <see cref="PasswordResetRequest"/> class with specified details.
    /// </summary>
    /// <param name="userId">The user identifier.</param>
    /// <param name="newPassword">The new password.</param>
    public PasswordResetRequest(long userId, string newPassword)
    {
        UserId = userId;
        NewPassword = newPassword;
    }
}

/// <summary>
/// Represents a request to update a user's profile information.
/// </summary>
public class ProfileUpdateRequest : WarrantyBee.Shared.Core.Contracts.BaseRequest
{
    /// <summary>
    /// Gets or sets the user's unique identifier.
    /// </summary>
    public long UserId { get; set; }

    /// <summary>
    /// Gets or sets the first line of the user's address.
    /// </summary>
    public string? AddressLine1 { get; set; }

    /// <summary>
    /// Gets or sets the second line of the user's address.
    /// </summary>
    public string? AddressLine2 { get; set; }

    /// <summary>
    /// Gets or sets the user's city.
    /// </summary>
    public string? City { get; set; }

    /// <summary>
    /// Gets or sets the user's postal code.
    /// </summary>
    public string? PostalCode { get; set; }

    /// <summary>
    /// Gets or sets the unique identifier of the region.
    /// </summary>
    public long? RegionId { get; set; }

    /// <summary>
    /// Gets or sets the unique identifier of the country.
    /// </summary>
    public long? CountryId { get; set; }

    /// <summary>
    /// Gets or sets the unique identifier of the culture.
    /// </summary>
    public long? CultureId { get; set; }

    /// <summary>
    /// Gets or sets the user's phone number.
    /// </summary>
    public string? PhoneNumber { get; set; }

    /// <summary>
    /// Gets or sets the user's international phone dialing code.
    /// </summary>
    public string? PhoneCode { get; set; }

    /// <summary>
    /// Gets or sets the URL of the user's avatar.
    /// </summary>
    public string? AvatarUrl { get; set; }
}

/// <summary>
/// Defines the contract for user-related persistence operations.
/// </summary>
public interface IUserRepository
{
    /// <summary>
    /// Creates a new user based on the sign-up request.
    /// </summary>
    /// <param name="request">The sign-up request.</param>
    /// <returns>The unique identifier of the newly created user.</returns>
    Task<long> CreateAsync(SignUpRequest request);

    /// <summary>
    /// Creates a new user with administrative privileges, allowing role specification.
    /// </summary>
    /// <param name="request">The administrative creation request.</param>
    /// <returns>The unique identifier of the newly created user.</returns>
    Task<long> AdminCreateUserAsync(AdminCreateUserRequest request);

    /// <summary>
    /// Retrieves a user based on the specified search filter.
    /// </summary>
    /// <param name="filter">The search filter.</param>
    /// <returns>The user response if found; otherwise, null.</returns>
    Task<UserResponse?> GetAsync(UserSearchFilter filter);

    /// <summary>
    /// Stores a login token for a user.
    /// </summary>
    /// <param name="details">The login token details.</param>
    /// <returns>True if the token was stored successfully; otherwise, false.</returns>
    Task<bool> StoreTokenAsync(LoginTokenDetails details);

    /// <summary>
    /// Validates a user's login token.
    /// </summary>
    /// <param name="details">The login token details.</param>
    /// <returns>True if the token is valid; otherwise, false.</returns>
    Task<bool> ValidateTokenAsync(LoginTokenDetails details);

    /// <summary>
    /// Resets a user's password.
    /// </summary>
    /// <param name="request">The password reset request.</param>
    /// <returns>True if the password was reset successfully; otherwise, false.</returns>
    Task<bool> ResetPasswordAsync(PasswordResetRequest request);

    /// <summary>
    /// Retrieves the historical passwords for a user.
    /// </summary>
    /// <param name="userId">The user identifier.</param>
    /// <returns>A collection of hashed passwords.</returns>
    Task<IEnumerable<string>> GetPasswordsAsync(long userId);

    /// <summary>
    /// Updates a user's profile information.
    /// </summary>
    /// <param name="request">The profile update request.</param>
    /// <returns>True if the profile was updated successfully; otherwise, false.</returns>
    Task<bool> UpdateProfileAsync(ProfileUpdateRequest request);
}

