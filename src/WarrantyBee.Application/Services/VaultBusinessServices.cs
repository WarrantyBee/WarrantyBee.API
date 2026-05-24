using WarrantyBee.Application.Abstractions.Persistence;
using WarrantyBee.Application.Abstractions.Services;
using WarrantyBee.Domain.Entities;
using WarrantyBee.Shared.Infrastructure.Abstractions;
using WarrantyBee.Shared.Core.Exceptions;

namespace WarrantyBee.Application.Services;

public class VaultService : IVaultService
{
    private readonly IVaultRepository _vaultRepository;
    private readonly IProductRepository _productRepository;
    private readonly ICurrentUserContext _userContext;

    public VaultService(IVaultRepository vaultRepository, IProductRepository productRepository, ICurrentUserContext userContext)
    {
        _vaultRepository = vaultRepository;
        _productRepository = productRepository;
        _userContext = userContext;
    }

    public async Task<IEnumerable<UserAppliance>> GetMyVaultAsync()
    {
        var userId = _userContext.UserId ?? 0;
        return await _vaultRepository.GetByUserIdAsync(userId);
    }

    public async Task<UserAppliance> RegisterApplianceAsync(RegisterApplianceRequest request)
    {
        var product = await _productRepository.GetBySkuAsync(request.Sku);
        if (product == null) throw new ApiException(Errors.ResourceNotFound, "Product SKU not found.");

        var userId = _userContext.UserId ?? 0;
        var warrantyEndDate = request.PurchaseDate.AddMonths(product.DefaultWarrantyMonths);

        var appliance = new UserAppliance
        {
            UserId = userId,
            ProductId = product.Id,
            SerialNumber = request.SerialNumber,
            PurchaseDate = request.PurchaseDate,
            WarrantyEndDate = warrantyEndDate,
            ReceiptUrl = request.ReceiptUrl,
            Status = "ACTIVE"
        };

        var id = await _vaultRepository.CreateAsync(appliance);
        appliance.Id = id;
        appliance.ProductName = product.Name;
        appliance.ProductCategory = product.Category;

        return appliance;
    }
}

public class ClaimService : IClaimService
{
    private readonly IClaimRepository _claimRepository;
    private readonly IVaultRepository _vaultRepository;
    private readonly IProductRepository _productRepository;
    private readonly IBusinessRepository _businessRepository;
    private readonly ICurrentUserContext _userContext;
    private readonly IEventPublisher _eventPublisher;

    public ClaimService(
        IClaimRepository claimRepository, 
        IVaultRepository vaultRepository,
        IProductRepository productRepository,
        IBusinessRepository businessRepository,
        ICurrentUserContext userContext,
        IEventPublisher eventPublisher)
    {
        _claimRepository = claimRepository;
        _vaultRepository = vaultRepository;
        _productRepository = productRepository;
        _businessRepository = businessRepository;
        _userContext = userContext;
        _eventPublisher = eventPublisher;
    }

    public async Task<IEnumerable<Claim>> GetMyClaimsAsync()
    {
        return await _claimRepository.GetByCustomerIdAsync(_userContext.UserId ?? 0);
    }

    public async Task<IEnumerable<Claim>> GetBusinessClaimsAsync()
    {
        // Get the business ID for the current logged in staff member
        // NOTE: BusinessId needs to be in ICurrentUserContext
        // For MVP, we'll assume the BusinessOwner/Staff has it
        var business = await _businessRepository.GetByOwnerIdAsync(_userContext.UserId ?? 0);
        if (business == null) return [];

        return await _claimRepository.GetByBusinessIdAsync(business.Id);
    }

    public async Task<Claim> FileClaimAsync(FileClaimRequest request)
    {
        var appliance = await _vaultRepository.GetByIdAsync(request.UserApplianceId);
        if (appliance == null || appliance.UserId != _userContext.UserId)
            throw new ApiException(Errors.UnauthorizedAccess);

        var product = await _productRepository_for_business_lookup(appliance.ProductId);

        var claimNumber = $"CLM-{DateTime.UtcNow:yyyyMMdd}-{Guid.NewGuid().ToString("N").Substring(0, 6).ToUpper()}";
        var claim = new Claim
        {
            ClaimNumber = claimNumber,
            UserApplianceId = request.UserApplianceId,
            CustomerId = _userContext.UserId ?? 0,
            BusinessId = product.BusinessId,
            IssueCategory = request.IssueCategory,
            IssueDescription = request.IssueDescription,
            DefectMediaUrl = request.DefectMediaUrl,
            Status = "SUBMITTED"
        };

        var id = await _claimRepository.CreateAsync(claim);
        claim.Id = id;

        // Update appliance status
        await _vaultRepository.UpdateStatusAsync(appliance.Id, "CLAIM_PENDING");

        // Notify Brand
        await _eventPublisher.PublishAsync("claim.submitted", new { ClaimId = id, BusinessId = product.BusinessId });

        return claim;
    }

    public async Task<Claim> GetClaimDetailsAsync(long id)
    {
        var claim = await _claimRepository.GetByIdAsync(id);
        if (claim == null) throw new ApiException(Errors.ResourceNotFound);
        return claim;
    }

    public async Task UpdateClaimStatusAsync(long id, string status, string? notes)
    {
        await _claimRepository.UpdateStatusAsync(id, status, notes);
        
        // Notify Customer via Event
        await _eventPublisher.PublishAsync("claim.status_changed", new { ClaimId = id, NewStatus = status });
    }

    public async Task AddMessageAsync(long claimId, string message, string? attachmentUrl)
    {
        await _claimRepository.AddMessageAsync(new ClaimMessage
        {
            ClaimId = claimId,
            SenderUserId = _userContext.UserId ?? 0,
            Message = message,
            AttachmentUrl = attachmentUrl
        });
    }

    public async Task<IEnumerable<ClaimMessage>> GetMessagesAsync(long claimId)
    {
        return await _claimRepository.GetMessagesByClaimIdAsync(claimId);
    }

    // Helper logic
    private async Task<Product> _productRepository_for_business_lookup(long productId)
    {
        var product = await _productRepository.GetByIdAsync(productId);
        if (product == null) throw new ApiException(Errors.ResourceNotFound);
        return product;
    }
}

public class ProductCatalogService : IProductCatalogService
{
    private readonly IProductRepository _productRepository;
    private readonly IBusinessRepository _businessRepository;
    private readonly ICurrentUserContext _userContext;

    public ProductCatalogService(IProductRepository productRepository, IBusinessRepository businessRepository, ICurrentUserContext userContext)
    {
        _productRepository = productRepository;
        _businessRepository = businessRepository;
        _userContext = userContext;
    }

    public async Task<IEnumerable<Product>> GetMyProductsAsync()
    {
        var business = await _businessRepository.GetByOwnerIdAsync(_userContext.UserId ?? 0);
        if (business == null) return [];
        return await _productRepository.GetByBusinessIdAsync(business.Id);
    }

    public async Task<Product> AddProductAsync(AddProductRequest request)
    {
        var business = await _businessRepository.GetByOwnerIdAsync(_userContext.UserId ?? 0);
        if (business == null) throw new ApiException(Errors.UnauthorizedAccess, "User is not associated with a business.");

        var product = new Product
        {
            BusinessId = business.Id,
            Sku = request.Sku,
            Name = request.Name,
            Category = request.Category,
            ImageUrl = request.ImageUrl,
            DefaultWarrantyMonths = request.DefaultWarrantyMonths
        };

        var id = await _productRepository.CreateAsync(product);
        product.Id = id;
        return product;
    }
}
