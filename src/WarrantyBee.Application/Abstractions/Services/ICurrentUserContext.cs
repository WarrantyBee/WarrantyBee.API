using WarrantyBee.Domain.Enums;

namespace WarrantyBee.Application.Abstractions.Services;

/// <summary>
/// Defines the contract for accessing information about the currently authenticated user.
/// </summary>
public interface ICurrentUserContext
{
    /// <summary>
    /// Gets the unique identifier of the current user.
    /// </summary>
    long? UserId { get; }

    /// <summary>
    /// Gets the email address of the current user.
    /// </summary>
    string? Email { get; }

    /// <summary>
    /// Gets the security role of the current user.
    /// </summary>
    SecurityRole Role { get; }

    /// <summary>
    /// Gets the collection of security permissions granted to the current user.
    /// </summary>
    IEnumerable<SecurityPermission> Permissions { get; }
}
