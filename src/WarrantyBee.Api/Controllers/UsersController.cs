using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Authorization;
using WarrantyBee.Application.Abstractions.Services;
using WarrantyBee.Application.Abstractions.Persistence;

namespace WarrantyBee.Api.Controllers;

[Authorize]
[Route("api/[controller]")]
public class UsersController : BaseController
{
    private readonly IUserService _userService;

    public UsersController(IUserService userService)
    {
        _userService = userService;
    }

    [HttpGet("profile")]
    public async Task<IActionResult> Get()
    {
        var user = await _userService.GetAsync();
        return OkResponse(user);
    }

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

    [HttpPatch("profile")]
    public async Task<IActionResult> UpdateProfile([FromBody] ProfileUpdateRequest request)
    {
        await _userService.UpdateProfileAsync(request);
        return OkResponse<object?>(null);
    }
}
