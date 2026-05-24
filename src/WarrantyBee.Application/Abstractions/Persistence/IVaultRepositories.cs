using WarrantyBee.Domain.Entities;

namespace WarrantyBee.Application.Abstractions.Persistence;

public interface IProductRepository
{
    Task<Product?> GetByIdAsync(long id);
    Task<Product?> GetBySkuAsync(string sku);
    Task<IEnumerable<Product>> GetByBusinessIdAsync(long businessId);
    Task<long> CreateAsync(Product product);
    Task UpdateAsync(Product product);
}

public interface IVaultRepository
{
    Task<UserAppliance?> GetByIdAsync(long id);
    Task<IEnumerable<UserAppliance>> GetByUserIdAsync(long userId);
    Task<long> CreateAsync(UserAppliance appliance);
    Task UpdateStatusAsync(long id, string status);
}

public interface IClaimRepository
{
    Task<Claim?> GetByIdAsync(long id);
    Task<Claim?> GetByClaimNumberAsync(string claimNumber);
    Task<IEnumerable<Claim>> GetByCustomerIdAsync(long customerId);
    Task<IEnumerable<Claim>> GetByBusinessIdAsync(long businessId);
    Task<long> CreateAsync(Claim claim);
    Task UpdateStatusAsync(long id, string status, string? resolutionNotes = null);
    
    Task AddMessageAsync(ClaimMessage message);
    Task<IEnumerable<ClaimMessage>> GetMessagesByClaimIdAsync(long claimId);
}
