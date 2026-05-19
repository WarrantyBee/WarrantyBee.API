using WarrantyBee.Domain.Enums;

namespace WarrantyBee.Application.Abstractions.Services;

public interface ICurrentUserContext
{
    long? UserId { get; }
    string? Email { get; }
    SecurityRole Role { get; }
    IEnumerable<SecurityPermission> Permissions { get; }
}
