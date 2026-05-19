namespace WarrantyBee.Domain.Enums;

/// <summary>
/// Represents the reasons for requesting a One-Time Password (OTP).
/// </summary>
public enum OtpRequestReason
{
    /// <summary>
    /// No reason specified.
    /// </summary>
    None = 0,
    /// <summary>
    /// OTP requested for login purposes.
    /// </summary>
    Login = 1,
    /// <summary>
    /// OTP requested for resetting a forgotten password.
    /// </summary>
    ForgotPassword = 2
}
