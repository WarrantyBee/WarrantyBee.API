using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Authorization;
using WarrantyBee.Application.Abstractions.Services;
using WarrantyBee.Application.Abstractions.Persistence;

namespace WarrantyBee.Api.Controllers;

/// <summary>
/// Controller for managing user-related operations, including profile and avatar management.
/// </summary>
[Authorize]
[Route("api/[controller]")]
public class UsersController : BaseController
{
    private readonly IUserService _userService;

    /// <summary>
    /// Initializes a new instance of the <see cref="UsersController"/> class.
    /// </summary>
    /// <param name="userService">The service used for user operations.</param>
    public UsersController(IUserService userService)
    {
        _userService = userService;
    }

    /// <summary>
    /// Retrieves the profile of the currently authenticated user.
    /// </summary>
    /// <returns>The user profile data.</returns>
    [HttpGet("profile")]
    public async Task<IActionResult> Get()
    {
        var user = await _userService.GetAsync();
        return OkResponse(user);
    }

    /// <summary>
    /// Changes the avatar for the user.
    /// </summary>
    /// <param name="avatar">The image file for the new avatar.</param>
    /// <param name="captchaResponse">The captcha response for validation.</param>
    /// <returns>A result indicating the success of the avatar update.</returns>
    [HttpPost("profile/changeavatar")]
    public async Task<IActionResult> ChangeAvatar([FromForm] IFormFile avatar, [FromForm] string captchaResponse)
    {
        // In a real app, userId comes from UserContext.
        // For now, I'll pass a dummy or 0 if I haven't implemented ICurrentUserContext yet.
        long userId = 0; 
        using var stream = avatar.OpenReadStream();
        var result = await _userService.ChangeAvatarAsync(userId, stream, avatar.FileName, avatar.ContentType);
        return OkResponse(result);
    }

    /// <summary>
    /// Updates the user's profile information.
    /// </summary>
    /// <param name="request">The profile update request data.</param>
    /// <returns>A successful response if the update was processed.</returns>
    [HttpPatch("profile")]
    public async Task<IActionResult> UpdateProfile([FromBody] ProfileUpdateRequest request)
    {
        await _userService.UpdateProfileAsync(request);
        return OkResponse<object?>(null);
    }
}
