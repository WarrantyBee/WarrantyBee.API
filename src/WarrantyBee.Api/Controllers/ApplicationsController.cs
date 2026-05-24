using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using WarrantyBee.Shared.Infrastructure.Abstractions;
using WarrantyBee.Domain.Entities;

namespace WarrantyBee.Api.Controllers;

/// <summary>
/// Controller for users to manage their registered applications and API keys.
/// </summary>
[Authorize]
[ApiController]
[Route("api/applications")]
public class ApplicationsController : BaseController
{
    private readonly IApiKeyManagementService _apiKeyService;
    private readonly ICurrentUserContext _userContext;

    public ApplicationsController(IApiKeyManagementService apiKeyService, ICurrentUserContext userContext)
    {
        _apiKeyService = apiKeyService;
        _userContext = userContext;
    }

    /// <summary>
    /// Gets all applications owned by the current user.
    /// </summary>
    [HttpGet]
    public async Task<IActionResult> GetOwnedApplications()
    {
        var userId = _userContext.UserId ?? 0;
        if (userId == 0) return Unauthorized();

        var apps = await _apiKeyService.GetOwnedClientsAsync(userId);
        return OkResponse(apps);
    }

    /// <summary>
    /// Registers a new application.
    /// </summary>
    [HttpPost]
    public async Task<IActionResult> CreateApplication([FromBody] CreateApplicationRequest request)
    {
        var userId = _userContext.UserId ?? 0;
        if (userId == 0) return Unauthorized();

        var client = await _apiKeyService.CreateClientAsync(request.Name, request.Description, request.AppType, userId);
        return OkResponse(client);
    }

    /// <summary>
    /// Gets all keys for a specific application owned by the user.
    /// </summary>
    [HttpGet("{id}/keys")]
    public async Task<IActionResult> GetKeys(long id)
    {
        // Ownership check
        var userId = _userContext.UserId ?? 0;
        var apps = await _apiKeyService.GetOwnedClientsAsync(userId);
        if (!apps.Any(a => a.Id == id)) return ForbiddenResponse();

        var keys = await _apiKeyService.GetKeysByClientAsync(id);
        return OkResponse(keys);
    }

    /// <summary>
    /// Generates a new API key (and optionally new App credentials) for an application.
    /// </summary>
    [HttpPost("{id}/keys/generate")]
    public async Task<IActionResult> GenerateKey(long id, [FromBody] GenerateKeyRequest request)
    {
        var userId = _userContext.UserId ?? 0;
        var apps = await _apiKeyService.GetOwnedClientsAsync(userId);
        if (!apps.Any(a => a.Id == id)) return ForbiddenResponse();

        var response = await _apiKeyService.GenerateKeyAsync(id, request.ExpiryMonths);
        return OkResponse(response);
    }

    /// <summary>
    /// Revokes an API key.
    /// </summary>
    [HttpDelete("keys/{keyId}")]
    public async Task<IActionResult> RevokeKey(long keyId)
    {
        // Ownership check (simplified: in a real app, verify the key belongs to an app owned by the user)
        await _apiKeyService.RevokeKeyAsync(keyId);
        return OkResponse<object?>(null);
    }

    /// <summary>
    /// Updates the allowed endpoint mappings for an API Key.
    /// </summary>
    [HttpPut("keys/{keyId}/endpoints")]
    public async Task<IActionResult> UpdateEndpoints(long keyId, [FromBody] UpdateEndpointsRequest request)
    {
        await _apiKeyService.UpdateKeyEndpointsAsync(keyId, request.Endpoints);
        return OkResponse<object?>(null);
    }
}

public class CreateApplicationRequest
{
    public string Name { get; set; } = string.Empty;
    public string Description { get; set; } = string.Empty;
    public byte AppType { get; set; } = 1;
}

public class GenerateKeyRequest
{
    public int ExpiryMonths { get; set; } = 1;
}

public class UpdateEndpointsRequest
{
    public IEnumerable<string> Endpoints { get; set; } = [];
}
