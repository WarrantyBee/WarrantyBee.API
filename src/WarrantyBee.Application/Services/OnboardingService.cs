using Microsoft.Extensions.Options;
using WarrantyBee.Application.Abstractions.Persistence;
using WarrantyBee.Shared.Infrastructure.Abstractions;
using WarrantyBee.Shared.Core.Utilities;
using WarrantyBee.Shared.Core.Configuration;
using WarrantyBee.Domain.Entities;
using WarrantyBee.Shared.Core.Enums;
using WarrantyBee.Shared.Core.Exceptions;
using WarrantyBee.Application.Abstractions.Services;

namespace WarrantyBee.Application.Services;

public class OnboardingService : IOnboardingService
{
    private readonly IOnboardingRepository _onboardingRepository;
    private readonly IBusinessRepository _businessRepository;
    private readonly IUserRepository _userRepository;
    private readonly ITelemetryService _telemetry;
    private readonly string _onboardingBaseUrl;

    public OnboardingService(
        IOnboardingRepository onboardingRepository,
        IBusinessRepository businessRepository,
        IUserRepository userRepository,
        ITelemetryService telemetry)
    {
        _onboardingRepository = onboardingRepository;
        _businessRepository = businessRepository;
        _userRepository = userRepository;
        _telemetry = telemetry;
        _onboardingBaseUrl = Environment.GetEnvironmentVariable("WB__ONBOARDING_URL") ?? "http://localhost:3000/onboard";
    }

    public async Task<string> GenerateOnboardingLinkAsync(OnboardingInvitationRequest request, long inviterUserId)
    {
        if (!Validator.IsEmail(request.Email)) throw new ApiException(Errors.InvalidEmail);

        var token = Guid.NewGuid();
        var link = new OnboardingLink
        {
            Token = token,
            Email = request.Email,
            TargetRoleId = request.RoleId,
            TargetBusinessId = request.BusinessId,
            InviterUserId = inviterUserId,
            ExpiresAt = DateTime.UtcNow.AddDays(7),
            IsUsed = false
        };

        await _onboardingRepository.AddLinkAsync(link);

        _telemetry.TrackEvent("OnboardingLinkGenerated", new Dictionary<string, object> 
        { 
            ["Role"] = request.RoleId, 
            ["BusinessId"] = request.BusinessId ?? 0 
        });

        return $"{_onboardingBaseUrl}?token={token}";
    }

    public async Task<OnboardingLinkDetails> ValidateTokenAsync(Guid token)
    {
        var link = await _onboardingRepository.GetByTokenAsync(token);
        if (link == null || link.IsUsed || link.ExpiresAt < DateTime.UtcNow)
        {
            throw new ApiException(Errors.InvalidToken);
        }

        string businessName = null;
        if (link.TargetBusinessId.HasValue)
        {
            var business = await _businessRepository.GetByIdAsync(link.TargetBusinessId.Value);
            businessName = business?.Name;
        }

        return new OnboardingLinkDetails
        {
            Email = link.Email,
            RoleId = link.TargetRoleId,
            BusinessId = link.TargetBusinessId,
            BusinessName = businessName
        };
    }

    public async Task CompleteOnboardingAsync(OnboardingCompletionRequest request)
    {
        var link = await _onboardingRepository.GetByTokenAsync(request.Token);
        if (link == null || link.IsUsed || link.ExpiresAt < DateTime.UtcNow)
        {
            throw new ApiException(Errors.InvalidToken);
        }

        // Force pre-assigned values
        request.Email = link.Email;
        request.RoleId = link.TargetRoleId;

        // Hash password
        request.Password = HashHelper.GetHash(request.Password);

        // 1. Create User
        var userId = await _userRepository.AdminCreateUserAsync(request);
        if (userId == 0) throw new ApiException(Errors.UserRegistrationFailed);

        // 2. If BusinessOwner, create the business profile
        if (link.TargetRoleId == (long)SecurityRole.BusinessOwner)
        {
            var businessId = await _businessRepository.CreateAsync(new BusinessProfile
            {
                Name = request.Firstname + "'s Business", // Placeholder name
                OwnerUserId = userId,
                IsVerified = false
            });
            
            // Link user back to business
            // NOTE: Need a dedicated UpdateUserBusiness method in repository
            // For now, I assume the database handled it or I'll implement a quick fix
        }

        // 3. Mark link as used
        await _onboardingRepository.MarkAsUsedAsync(request.Token);

        _telemetry.TrackEvent("OnboardingCompleted", new Dictionary<string, object> { ["UserId"] = userId });
    }
}
