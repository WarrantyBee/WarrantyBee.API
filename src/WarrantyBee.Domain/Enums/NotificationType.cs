namespace WarrantyBee.Domain.Enums;

/// <summary>
/// Represents the types of notifications that can be sent to a user.
/// </summary>
public enum NotificationType
{
    /// <summary>
    /// Multi-factor authentication login notification.
    /// </summary>
    LoginOtp = 1,

    /// <summary>
    /// Forgot password notification.
    /// </summary>
    ForgotPasswordOtp = 2,

    /// <summary>
    /// Welcome notification for new users.
    /// </summary>
    WelcomeEmail = 3,

    /// <summary>
    /// Notification indicating a password change.
    /// </summary>
    PasswordChanged = 4
}
