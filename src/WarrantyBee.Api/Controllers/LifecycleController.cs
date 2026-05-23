using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace WarrantyBee.Api.Controllers;

/// <summary>
/// Controller for lifecycle and health checks of the API.
/// </summary>
[Authorize]
[Route("api/alive")]
public class LifecycleController : BaseController
{
    /// <summary>
    /// Validates the presence and validity of the authorization header.
    /// </summary>
    /// <returns>A successful API response if the token is valid.</returns>
    [HttpPost]
    public IActionResult Alive()
    {
        return OkResponse<object?>(null);
    }
}

