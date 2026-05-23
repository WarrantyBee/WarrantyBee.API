using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using WarrantyBee.Application.Abstractions.Services;

namespace WarrantyBee.Api.Controllers.Admin;

/// <summary>
/// Administrative controller for managing API keys and clients across the ecosystem.
/// </summary>
[Authorize(Roles = "SUPER_ADMIN")]
[ApiController]
[Route("api/admin/keys")]
public class ApiKeyController : BaseController
{
    private readonly IApiKeyManagementService _apiKeyService;

    public ApiKeyController(IApiKeyManagementService apiKeyService)
    {
        _apiKeyService = apiKeyService;
    }

    /// <summary>
    /// Gets all registered API clients.
    /// </summary>
    /// <returns>A list of API clients.</returns>
    [HttpGet("clients")]
    public async Task<IActionResult> GetClients()
    {
        var clients = await _apiKeyService.GetClientsAsync();
        return OkResponse(clients);
    }

    /// <summary>
    /// Gets all API keys for a specific client.
    /// </summary>
    /// <param name="clientId">The ID of the client.</param>
    /// <returns>A list of API keys.</returns>
    [HttpGet("clients/{clientId}")]
    public async Task<IActionResult> GetKeys(long clientId)
    {
        var keys = await _apiKeyService.GetKeysByClientAsync(clientId);
        return OkResponse(keys);
    }

    /// <summary>
    /// Generates a new API key for a client.
    /// </summary>
    /// <param name="request">The generation request.</param>
    /// <returns>The generated AppId and plain-text AppSecret.</returns>
    [HttpPost("generate")]
    public async Task<IActionResult> GenerateKey([FromBody] GenerateKeyRequest request)
    {
        var response = await _apiKeyService.GenerateKeyAsync(request.ClientId, request.ExpiryMonths);
        return OkResponse(response);
    }

    /// <summary>
    /// Revokes an API key immediately.
    /// </summary>
    /// <param name="id">The ID of the API key to revoke.</param>
    /// <returns>A success response.</returns>
    [HttpDelete("{id}")]
    public async Task<IActionResult> RevokeKey(long id)
    {
        await _apiKeyService.RevokeKeyAsync(id);
        return OkResponse<object?>(null);
    }
}

public class GenerateKeyRequest
{
    public long ClientId { get; set; }
    public int ExpiryMonths { get; set; } = 1;
}
