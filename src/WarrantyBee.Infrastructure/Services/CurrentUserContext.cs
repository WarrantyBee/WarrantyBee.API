using System.Security.Claims;
using Microsoft.AspNetCore.Http;
using WarrantyBee.Application.Abstractions.Services;
using WarrantyBee.Domain.Enums;

namespace WarrantyBee.Infrastructure.Services;

/// <summary>
/// Provides access to the current authenticated user's context, including identity and permissions.
/// </summary>
public class CurrentUserContext : ICurrentUserContext
{
    private readonly IHttpContextAccessor _httpContextAccessor;

    /// <summary>
    /// Initializes a new instance of the <see cref="CurrentUserContext"/> class.
    /// </summary>
    /// <param name="httpContextAccessor">The HTTP context accessor to retrieve the current user's identity.</param>
    public CurrentUserContext(IHttpContextAccessor httpContextAccessor)
    {
        _httpContextAccessor = httpContextAccessor;
    }

    /// <summary>
    /// Gets the unique identifier of the current user, if authenticated.
    /// </summary>
    public long? UserId
    {
        get
        {
            var userIdStr = _httpContextAccessor.HttpContext?.User?.FindFirstValue("userId");
            return long.TryParse(userIdStr, out var userId) ? userId : null;
        }
    }

    /// <summary>
    /// Gets the email address of the current user, if authenticated.
    /// </summary>
    public string? Email => _httpContextAccessor.HttpContext?.User?.FindFirstValue(ClaimTypes.Email) 
                           ?? _httpContextAccessor.HttpContext?.User?.FindFirstValue("email");

    /// <summary>
    /// Gets the security role assigned to the current user.
    /// </summary>
    public SecurityRole Role
    {
        get
        {
            var roleStr = _httpContextAccessor.HttpContext?.User?.FindFirstValue(ClaimTypes.Role)
                         ?? _httpContextAccessor.HttpContext?.User?.FindFirstValue("role");
            return Enum.TryParse<SecurityRole>(roleStr, true, out var role) ? role : SecurityRole.None;
        }
    }

    /// <summary>
    /// Gets the collection of security permissions assigned to the current user.
    /// </summary>
    public IEnumerable<SecurityPermission> Permissions
    {
        get
        {
            var permissionsStr = _httpContextAccessor.HttpContext?.User?.FindFirstValue("permissions");
            if (string.IsNullOrWhiteSpace(permissionsStr)) return [];
            return permissionsStr.Split(',')
                .Select(p => Enum.TryParse<SecurityPermission>(p.Trim(), true, out var perm) ? perm : SecurityPermission.None)
                .Where(p => p != SecurityPermission.None);
        }
    }
}
