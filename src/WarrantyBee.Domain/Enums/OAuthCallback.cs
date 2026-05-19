namespace WarrantyBee.Domain.Enums;

/// <summary>
/// Represents the callback actions for OAuth authentication.
/// </summary>
public enum OAuthCallback
{
    /// <summary>
    /// No callback action specified.
    /// </summary>
    None = 0,
    /// <summary>
    /// Sign up callback action.
    /// </summary>
    SignUp = 1,
    /// <summary>
    /// Sign in callback action.
    /// </summary>
    SignIn = 2
}
