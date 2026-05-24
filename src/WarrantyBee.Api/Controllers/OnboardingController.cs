using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using WarrantyBee.Application.Abstractions.Services;
using WarrantyBee.Shared.Infrastructure.Abstractions;
using WarrantyBee.Shared.Core.Enums;

namespace WarrantyBee.Api.Controllers;

/// <summary>
/// Controller for managing secure invitation-based onboarding for businesses and staff.
/// </summary>
[ApiController]
[Route("api/onboarding")]
public class OnboardingController : BaseController
{
    private readonly IOnboardingService _onboardingService;
    private readonly ICurrentUserContext _userContext;

    public OnboardingController(IOnboardingService onboardingService, ICurrentUserContext userContext)
    {
        _onboardingService = onboardingService;
        _userContext = userContext;
    }

    /// <summary>
    /// Generates a secure invitation link for a new business or staff member.
    /// Requires PlatformAdmin, BusinessOwner, or BusinessAdmin.
    /// </summary>
    [Authorize(Roles = "PlatformAdmin,BusinessOwner,BusinessAdmin")]
    [HttpPost("invite")]
    public async Task<IActionResult> Invite([FromBody] OnboardingInvitationRequest request)
    {
        var inviterId = _userContext.UserId ?? 0;
        if (inviterId == 0) return Unauthorized();

        // Business logic: Business owners can only invite staff to their own business
        if (_userContext.Role == SecurityRole.BusinessOwner || _userContext.Role == SecurityRole.BusinessAdmin)
        {
            // Security: Prevent staff from inviting themselves as owners
            if (request.RoleId == (long)SecurityRole.BusinessOwner || request.RoleId == (long)SecurityRole.PlatformAdmin)
            {
                return ForbiddenResponse();
            }
            
            // Hardcode their own business ID for staff invitations
            // NOTE: Need to add BusinessId to ICurrentUserContext if we want this check
        }

        var link = await _onboardingService.GenerateOnboardingLinkAsync(request, inviterId);
        return OkResponse(new { Link = link });
    }

    /// <summary>
    /// Validates an onboarding token and returns the pre-assigned email and role.
    /// Publicly accessible but only for valid tokens.
    /// </summary>
    [HttpGet("validate")]
    public async Task<IActionResult> Validate([FromQuery] Guid token)
    {
        var details = await _onboardingService.ValidateTokenAsync(token);
        return OkResponse(details);
    }

    /// <summary>
    /// Completes the onboarding process, creating the user and potentially the business profile.
    /// </summary>
    [HttpPost("complete")]
    public async Task<IActionResult> Complete([FromBody] OnboardingCompletionRequest request)
    {
        await _onboardingService.CompleteOnboardingAsync(request);
        return OkResponse<object?>(null);
    }
}
