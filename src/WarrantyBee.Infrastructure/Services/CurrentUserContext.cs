using System.Security.Claims;
using Microsoft.AspNetCore.Http;
using WarrantyBee.Application.Abstractions.Services;
using WarrantyBee.Domain.Enums;

namespace WarrantyBee.Infrastructure.Services;

public class CurrentUserContext : ICurrentUserContext
{
    private readonly IHttpContextAccessor _httpContextAccessor;

    public CurrentUserContext(IHttpContextAccessor httpContextAccessor)
    {
        _httpContextAccessor = httpContextAccessor;
    }

    public long? UserId
    {
        get
        {
            var userIdStr = _httpContextAccessor.HttpContext?.User?.FindFirstValue("userId");
            return long.TryParse(userIdStr, out var userId) ? userId : null;
        }
    }

    public string? Email => _httpContextAccessor.HttpContext?.User?.FindFirstValue(ClaimTypes.Email) 
                           ?? _httpContextAccessor.HttpContext?.User?.FindFirstValue("email");

    public SecurityRole Role
    {
        get
        {
            var roleStr = _httpContextAccessor.HttpContext?.User?.FindFirstValue(ClaimTypes.Role)
                         ?? _httpContextAccessor.HttpContext?.User?.FindFirstValue("role");
            return Enum.TryParse<SecurityRole>(roleStr, true, out var role) ? role : SecurityRole.None;
        }
    }

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
