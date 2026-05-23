using WarrantyBee.Shared.Core.Contracts;

namespace WarrantyBee.Application.Contracts.Identity;

/// <summary>
/// Represents the base structure for all login-related requests.
/// </summary>
public abstract class LoginRequest : BaseRequest
{
    /// <summary>
    /// Gets or sets the email address of the user.
    /// </summary>
    public string Email { get; set; } = string.Empty;
}

/// <summary>
/// Represents a simple login request using email and password, or an authentication provider.
/// </summary>
public class SimpleLoginRequest : LoginRequest
{
    /// <summary>
    /// Gets or sets the user's password. Required for standard login.
    /// </summary>
    public string? Password { get; set; }

    /// <summary>
    /// Gets or sets the authentication provider (e.g., Google, Facebook).
    /// </summary>
    public byte? AuthProvider { get; set; }

    /// <summary>
    /// Gets or sets the unique identifier provided by the external authentication provider.
    /// </summary>
    public string? AuthProviderUserId { get; set; }
}

/// <summary>
/// Represents a login request for completing multi-factor authentication.
/// </summary>
public class MFALoginRequest : LoginRequest
{
    /// <summary>
    /// Gets or sets the short-lived login token issued during the first step of login.
    /// </summary>
    public string? Token { get; set; }

    /// <summary>
    /// Gets or sets the one-time password (OTP) received by the user.
    /// </summary>
    public string? Otp { get; set; }
}

