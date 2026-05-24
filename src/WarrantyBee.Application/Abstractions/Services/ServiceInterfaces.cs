using WarrantyBee.Application.Contracts.Identity;
using WarrantyBee.Application.Contracts.Users;
using WarrantyBee.Application.Contracts.Geographic;
using WarrantyBee.Shared.Core.Enums;
using WarrantyBee.Application.Abstractions.Services;

namespace WarrantyBee.Application.Abstractions.Services;

/// <summary>
/// Represents the payload for a notification to be sent to a user.
/// </summary>

/// <summary>
/// Defines the contract for authentication-related services.
/// </summary>
public interface IAuthService
{
    /// <summary>
    /// Authenticates a user based on the login request.
    /// </summary>
    /// <param name="request">The login request.</param>
    /// <returns>A login response containing the authentication result.</returns>
    Task<ILoginResponse> LoginAsync(LoginRequest request);

    /// <summary>
    /// Refreshes an access token using a refresh token.
    /// </summary>
    /// <param name="request">The refresh token request.</param>
    /// <returns>A login response containing new tokens.</returns>
    Task<LoginResponse> RefreshTokenAsync(RefreshTokenRequest request);

    /// <summary>
    /// Registers a new user based on the sign-up request.
    /// </summary>
    /// <param name="request">The sign-up request.</param>
    /// <returns>A sign-up response containing the registration result.</returns>
    Task<SignUpResponse> SignUpAsync(SignUpRequest request);

    /// <summary>
    /// Initiates the forgot password process for a user.
    /// </summary>
    /// <param name="request">The forgot password request.</param>
    /// <returns>A task representing the asynchronous operation.</returns>
    Task ForgotPasswordAsync(ForgotPasswordRequest request);

    /// <summary>
    /// Resets a user's password based on the reset password request.
    /// </summary>
    /// <param name="request">The reset password request.</param>
    /// <returns>A task representing the asynchronous operation.</returns>
    Task ResetPasswordAsync(ResetPasswordRequest request);
}

/// <summary>
/// Defines the contract for user-related services.
/// </summary>
public interface IUserService
{
    /// <summary>
    /// Retrieves the profile information of the current user.
    /// </summary>
    /// <returns>A user response if found; otherwise, null.</returns>
    Task<UserResponse?> GetAsync();

    /// <summary>
    /// Changes the avatar of a user.
    /// </summary>
    /// <param name="userId">The user identifier.</param>
    /// <param name="avatarStream">The stream of the avatar image file.</param>
    /// <param name="fileName">The name of the file.</param>
    /// <param name="contentType">The content type of the file.</param>
    /// <returns>An avatar response containing the new avatar URL.</returns>
    Task<AvatarResponse> ChangeAvatarAsync(long userId, Stream avatarStream, string fileName, string contentType);

    /// <summary>
    /// Updates the profile information of the current user.
    /// </summary>
    /// <param name="request">The profile update request.</param>
    /// <returns>A task representing the asynchronous operation.</returns>
    Task UpdateProfileAsync(Persistence.ProfileUpdateRequest request);

    /// <summary>
    /// Creates a new user with administrative privileges.
    /// </summary>
    /// <param name="request">The administrative user creation request.</param>
    /// <returns>A task representing the asynchronous operation.</returns>
    Task AdminCreateUserAsync(Contracts.Users.AdminCreateUserRequest request);
}

/// <summary>
/// Defines the contract for country-related services.
/// </summary>
public interface ICountryService
{
    /// <summary>
    /// Retrieves all countries with their detailed information.
    /// </summary>
    /// <returns>A collection of country details.</returns>
    Task<IEnumerable<Persistence.CountryDetailResponse>> GetAsync();
}

/// <summary>
/// Defines the contract for One-Time Password (OTP) generation.
/// </summary>
public interface IOtpService
{
    /// <summary>
    /// Generates a new OTP.
    /// </summary>
    /// <returns>A string representation of the generated OTP.</returns>
    string Generate();
}

/// <summary>
/// Defines the contract for CAPTCHA validation services.
/// </summary>

/// <summary>
/// Defines the contract for email sending services.
/// </summary>

/// <summary>
/// Defines the contract for email template processing services.
/// </summary>
public interface IEmailTemplateService
{
    /// <summary>
    /// Processes an email template by replacing macros with their corresponding values.
    /// </summary>
    /// <param name="templatePath">The path to the email template file.</param>
    /// <param name="macros">A dictionary of macros and their replacement values.</param>
    /// <returns>The processed email template content.</returns>
    string Process(string templatePath, IDictionary<string, string> macros);
}

/// <summary>
/// Defines the contract for file storage services.
/// </summary>

/// <summary>
/// Defines the contract for token-related services.
/// </summary>
public interface ITokenService
{
    /// <summary>
    /// Generates a token based on the specified claims.
    /// </summary>
    /// <param name="claims">A dictionary of claims to include in the token.</param>
    /// <returns>A string representation of the generated token.</returns>
    string Generate(IDictionary<string, object> claims);

    /// <summary>
    /// Validates a token and returns the claims it contains.
    /// </summary>
    /// <param name="token">The token to validate.</param>
    /// <returns>A dictionary of claims extracted from the token.</returns>
    IDictionary<string, object> Validate(string token);
}

/// <summary>
/// Represents a response containing an avatar URL.
/// </summary>
public class AvatarResponse
{
    /// <summary>
    /// Gets or sets the URL of the avatar image.
    /// </summary>
    public string Url { get; set; } = string.Empty;

    /// <summary>
    /// Initializes a new instance of the <see cref="AvatarResponse"/> class.
    /// </summary>
    public AvatarResponse() { }

    /// <summary>
    /// Initializes a new instance of the <see cref="AvatarResponse"/> class with a specified URL.
    /// </summary>
    /// <param name="url">The avatar URL.</param>
    public AvatarResponse(string url) => Url = url;
}
