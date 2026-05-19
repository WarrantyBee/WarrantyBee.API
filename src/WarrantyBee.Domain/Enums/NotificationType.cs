namespace WarrantyBee.Domain.Enums;

/// <summary>
/// Represents the types of notifications that can be sent to a user.
/// </summary>
public enum NotificationType
{
    /// <summary>
    /// No notification type specified.
    /// </summary>
    None = 0,
    /// <summary>
    /// Multi-factor authentication login notification.
    /// </summary>
    MfaLogin = 1,
    /// <summary>
    /// Forgot password notification.
    /// </summary>
    ForgotPassword = 2,
    /// <summary>
    /// Welcome notification for new users.
    /// </summary>
    Welcome = 3,
    /// <summary>
    /// Notification indicating a password change.
    /// </summary>
    PasswordChanged = 4
}
