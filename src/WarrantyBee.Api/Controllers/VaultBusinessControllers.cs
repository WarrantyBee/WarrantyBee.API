using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using WarrantyBee.Application.Abstractions.Services;
using WarrantyBee.Shared.Core.Enums;

namespace WarrantyBee.Api.Controllers;

[Authorize(Roles = "Customer")]
[ApiController]
[Route("api/vault")]
public class VaultController : BaseController
{
    private readonly IVaultService _vaultService;

    public VaultController(IVaultService vaultService)
    {
        _vaultService = vaultService;
    }

    [HttpGet]
    public async Task<IActionResult> GetVault()
    {
        var appliances = await _vaultService.GetMyVaultAsync();
        return OkResponse(appliances);
    }

    [HttpPost("register")]
    public async Task<IActionResult> RegisterAppliance([FromBody] RegisterApplianceRequest request)
    {
        var appliance = await _vaultService.RegisterApplianceAsync(request);
        return OkResponse(appliance);
    }
}

[Authorize]
[ApiController]
[Route("api/claims")]
public class ClaimsController : BaseController
{
    private readonly IClaimService _claimService;

    public ClaimsController(IClaimService claimService)
    {
        _claimService = claimService;
    }

    [Authorize(Roles = "Customer")]
    [HttpGet("my-claims")]
    public async Task<IActionResult> GetMyClaims()
    {
        var claims = await _claimService.GetMyClaimsAsync();
        return OkResponse(claims);
    }

    [Authorize(Roles = "BusinessAdmin,BrandSupport")]
    [HttpGet("b2b")]
    public async Task<IActionResult> GetBusinessClaims()
    {
        var claims = await _claimService.GetBusinessClaimsAsync();
        return OkResponse(claims);
    }

    [Authorize(Roles = "Customer")]
    [HttpPost]
    public async Task<IActionResult> FileClaim([FromBody] FileClaimRequest request)
    {
        var claim = await _claimService.FileClaimAsync(request);
        return OkResponse(claim);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetClaim(long id)
    {
        var claim = await _claimService.GetClaimDetailsAsync(id);
        return OkResponse(claim);
    }

    [Authorize(Roles = "BusinessAdmin,BrandSupport")]
    [HttpPatch("{id}/status")]
    public async Task<IActionResult> UpdateStatus(long id, [FromBody] UpdateClaimStatusRequest request)
    {
        await _claimService.UpdateClaimStatusAsync(id, request.Status, request.Notes);
        return OkResponse<object?>(null);
    }

    [HttpPost("{id}/messages")]
    public async Task<IActionResult> AddMessage(long id, [FromBody] AddClaimMessageRequest request)
    {
        await _claimService.AddMessageAsync(id, request.Message, request.AttachmentUrl);
        return OkResponse<object?>(null);
    }

    [HttpGet("{id}/messages")]
    public async Task<IActionResult> GetMessages(long id)
    {
        var messages = await _claimService.GetMessagesAsync(id);
        return OkResponse(messages);
    }
}

[Authorize(Roles = "BusinessAdmin,BusinessOwner")]
[ApiController]
[Route("api/catalog")]
public class CatalogController : BaseController
{
    private readonly IProductCatalogService _catalogService;

    public CatalogController(IProductCatalogService catalogService)
    {
        _catalogService = catalogService;
    }

    [HttpGet("products")]
    public async Task<IActionResult> GetProducts()
    {
        var products = await _catalogService.GetMyProductsAsync();
        return OkResponse(products);
    }

    [HttpPost("products")]
    public async Task<IActionResult> AddProduct([FromBody] AddProductRequest request)
    {
        var product = await _catalogService.AddProductAsync(request);
        return OkResponse(product);
    }
}

public class UpdateClaimStatusRequest
{
    public string Status { get; set; } = string.Empty;
    public string? Notes { get; set; }
}

public class AddClaimMessageRequest
{
    public string Message { get; set; } = string.Empty;
    public string? AttachmentUrl { get; set; }
}
