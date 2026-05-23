using System.Text.Json.Serialization;
using WarrantyBee.Shared.Core.Contracts;
using WarrantyBee.Application.Contracts.Users;

namespace WarrantyBee.Application.Contracts.Identity;

/// <summary>
/// Represents the response after a successful sign-up.
/// </summary>
public class SignUpResponse
{
    /// <summary>
    /// Gets or sets the unique identifier of the newly created user.
    /// </summary>
    public long Id { get; set; }

    /// <summary>
    /// Initializes a new instance of the <see cref="SignUpResponse"/> class.
    /// </summary>
    public SignUpResponse() { }

    /// <summary>
    /// Initializes a new instance of the <see cref="SignUpResponse"/> class with a specified identifier.
    /// </summary>
    /// <param name="id">The user identifier.</param>
    public SignUpResponse(long id) => Id = id;
}

/// <summary>
/// Defines the contract for login responses, supporting multiple types such as standard login and MFA.
/// </summary>
[JsonDerivedType(typeof(LoginResponse), typeDiscriminator: "login")]
[JsonDerivedType(typeof(MFALoginResponse), typeDiscriminator: "mfa")]
public interface ILoginResponse { }

/// <summary>
/// Represents a successful standard login response containing authentication tokens and user details.
/// </summary>
public class LoginResponse : ILoginResponse
{
    /// <summary>
    /// Gets or sets the JWT access token.
    /// </summary>
    public string AccessToken { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the timestamp when the token was issued.
    /// </summary>
    public string IssuedAt { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the timestamp when the token will expire.
    /// </summary>
    public string ExpiresAt { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the details of the authenticated user.
    /// </summary>
    public UserResponse? User { get; set; }

    /// <summary>
    /// Initializes a new instance of the <see cref="LoginResponse"/> class.
    /// </summary>
    public LoginResponse() { }

    /// <summary>
    /// Initializes a new instance of the <see cref="LoginResponse"/> class with detailed information.
    /// </summary>
    /// <param name="accessToken">The JWT access token.</param>
    /// <param name="issuedAt">The issuance timestamp.</param>
    /// <param name="expiresAt">The expiration timestamp.</param>
    /// <param name="user">The user details.</param>
    public LoginResponse(string accessToken, string issuedAt, string expiresAt, UserResponse user)
    {
        AccessToken = accessToken;
        IssuedAt = issuedAt;
        ExpiresAt = expiresAt;
        User = user;
    }
}

/// <summary>
/// Represents a login response when multi-factor authentication is required.
/// </summary>
public class MFALoginResponse : ILoginResponse
{
    /// <summary>
    /// Gets or sets the short-lived login token used to complete MFA.
    /// </summary>
    public string LoginToken { get; set; } = string.Empty;

    /// <summary>
    /// Initializes a new instance of the <see cref="MFALoginResponse"/> class.
    /// </summary>
    public MFALoginResponse() { }

    /// <summary>
    /// Initializes a new instance of the <see cref="MFALoginResponse"/> class with a login token.
    /// </summary>
    /// <param name="loginToken">The MFA login token.</param>
    public MFALoginResponse(string loginToken) => LoginToken = loginToken;
}

/// <summary>
/// Represents a request to initiate the forgot password process.
/// </summary>
public class ForgotPasswordRequest
{
    /// <summary>
    /// Gets or sets the email address of the user who forgot their password.
    /// </summary>
    public string Email { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the optional captcha response token for verification.
    /// </summary>
    public string? CaptchaResponse { get; set; }
}

/// <summary>
/// Represents a request to reset a user's password using an OTP.
/// </summary>
public class ResetPasswordRequest
{
    /// <summary>
    /// Gets or sets the one-time password received via email.
    /// </summary>
    public string Otp { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the email address of the user.
    /// </summary>
    public string Email { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the new password for the user.
    /// </summary>
    public string NewPassword { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the optional captcha response token for verification.
    /// </summary>
    public string? CaptchaResponse { get; set; }
}

