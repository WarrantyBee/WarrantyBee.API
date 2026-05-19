namespace WarrantyBee.Domain.Enums;

/// <summary>
/// Represents the supported authentication providers.
/// </summary>
public enum AuthProvider
{
    /// <summary>
    /// No provider specified.
    /// </summary>
    None = 0,
    /// <summary>
    /// Internal authentication system.
    /// </summary>
    Internal = 1,
    /// <summary>
    /// Facebook OAuth provider.
    /// </summary>
    Facebook = 2,
    /// <summary>
    /// Google OAuth provider.
    /// </summary>
    Google = 3,
    /// <summary>
    /// LinkedIn OAuth provider.
    /// </summary>
    Linkedin = 4
}
