using WarrantyBee.Application.Contracts.Users;
using WarrantyBee.Domain.Entities;

namespace WarrantyBee.Application.Abstractions.Services;

public interface IOnboardingService
{
    Task<string> GenerateOnboardingLinkAsync(OnboardingInvitationRequest request, long inviterUserId);
    Task<OnboardingLinkDetails> ValidateTokenAsync(Guid token);
    Task CompleteOnboardingAsync(OnboardingCompletionRequest request);
}

public class OnboardingInvitationRequest
{
    public string Email { get; set; } = string.Empty;
    public long RoleId { get; set; }
    public long? BusinessId { get; set; }
    public string? BrandName { get; set; } // Only for new BusinessOwner invitations
}

public class OnboardingLinkDetails
{
    public string Email { get; set; } = string.Empty;
    public long RoleId { get; set; }
    public string RoleName { get; set; } = string.Empty;
    public long? BusinessId { get; set; }
    public string? BusinessName { get; set; }
}

public class OnboardingCompletionRequest : AdminCreateUserRequest
{
    public Guid Token { get; set; }
}
