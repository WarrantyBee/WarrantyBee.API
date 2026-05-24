using WarrantyBee.Domain.Entities;

namespace WarrantyBee.Application.Abstractions.Persistence;

public interface IBusinessRepository
{
    Task<BusinessProfile?> GetByIdAsync(long id);
    Task<BusinessProfile?> GetByOwnerIdAsync(long ownerUserId);
    Task<IEnumerable<BusinessProfile>> GetPendingVerificationAsync();
    Task<long> CreateAsync(BusinessProfile profile);
    Task UpdateAsync(BusinessProfile profile);
    Task VerifyAsync(long id);
}

public interface IOnboardingRepository
{
    Task AddLinkAsync(OnboardingLink link);
    Task<OnboardingLink?> GetByTokenAsync(Guid token);
    Task MarkAsUsedAsync(Guid token);
}
