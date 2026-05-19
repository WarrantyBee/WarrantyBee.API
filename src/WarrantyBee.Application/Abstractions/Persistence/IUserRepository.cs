using WarrantyBee.Application.Contracts.Identity;
using WarrantyBee.Application.Contracts.Users;
using WarrantyBee.Application.Contracts.Geographic;

namespace WarrantyBee.Application.Abstractions.Persistence;

public class UserSearchFilter
{
    public long? Id { get; set; }
    public string? Email { get; set; }

    public UserSearchFilter() { }
    public UserSearchFilter(long? id, string? email)
    {
        Id = id;
        Email = email;
    }
}

public class LoginTokenDetails
{
    public long UserId { get; set; }
    public string Token { get; set; } = string.Empty;

    public LoginTokenDetails() { }
    public LoginTokenDetails(long userId, string token)
    {
        UserId = userId;
        Token = token;
    }
}

public class PasswordResetRequest
{
    public long UserId { get; set; }
    public string NewPassword { get; set; } = string.Empty;

    public PasswordResetRequest() { }
    public PasswordResetRequest(long userId, string newPassword)
    {
        UserId = userId;
        NewPassword = newPassword;
    }
}

public class ProfileUpdateRequest : Contracts.Common.BaseRequest
{
    public long UserId { get; set; }
    public string? AddressLine1 { get; set; }
    public string? AddressLine2 { get; set; }
    public string? City { get; set; }
    public string? PostalCode { get; set; }
    public long? RegionId { get; set; }
    public long? CountryId { get; set; }
    public long? CultureId { get; set; }
    public string? PhoneNumber { get; set; }
    public string? PhoneCode { get; set; }
    public string? AvatarUrl { get; set; }
}

public interface IUserRepository
{
    Task<long> CreateAsync(SignUpRequest request);
    Task<UserResponse?> GetAsync(UserSearchFilter filter);
    Task<bool> StoreTokenAsync(LoginTokenDetails details);
    Task<bool> ValidateTokenAsync(LoginTokenDetails details);
    Task<bool> ResetPasswordAsync(PasswordResetRequest request);
    Task<IEnumerable<string>> GetPasswordsAsync(long userId);
    Task<bool> UpdateProfileAsync(ProfileUpdateRequest request);
}
