using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using WarrantyBee.Application.Abstractions.Services;
using WarrantyBee.Application.Contracts.Users;
using WarrantyBee.Shared.Core.Enums;

namespace WarrantyBee.Api.Controllers.Admin;

/// <summary>
/// Administrative controller for managing users across the platform.
/// </summary>
[Authorize(Roles = "PlatformAdmin,BusinessOwner,BusinessAdmin")]
[ApiController]
[Route("api/admin/users")]
public class AdminUsersController : BaseController
{
    private readonly IUserService _userService;

    public AdminUsersController(IUserService userService)
    {
        _userService = userService;
    }

    /// <summary>
    /// Creates a new user with a specified role.
    /// Only accessible by Platform, Business Owners, and Business Admins.
    /// </summary>
    /// <param name="request">The user creation details.</param>
    /// <returns>A successful response if the user is created.</returns>
    [HttpPost]
    public async Task<IActionResult> CreateUser([FromBody] AdminCreateUserRequest request)
    {
        await _userService.AdminCreateUserAsync(request);
        return OkResponse<object?>(null);
    }
}
