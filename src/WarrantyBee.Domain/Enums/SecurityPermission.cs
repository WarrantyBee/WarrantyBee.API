namespace WarrantyBee.Domain.Enums;

/// <summary>
/// Represents the security permissions available in the system.
/// </summary>
public enum SecurityPermission
{
    /// <summary>
    /// No permission.
    /// </summary>
    None = 0,
    /// <summary>
    /// Permission to edit a user profile.
    /// </summary>
    EditProfile = 1,
    /// <summary>
    /// Permission to change an avatar.
    /// </summary>
    ChangeAvatar = 2,
    /// <summary>
    /// Permission to access a user profile.
    /// </summary>
    AccessProfile = 3
}
