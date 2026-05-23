using Microsoft.AspNetCore.Mvc;
using WarrantyBee.Shared.Core.Contracts;

namespace WarrantyBee.Api.Controllers;

/// <summary>
/// Provides a base class for API controllers with common functionality.
/// </summary>
[ApiController]
public abstract class BaseController : ControllerBase
{
    /// <summary>
    /// Returns a standardized successful API response.
    /// </summary>
    /// <typeparam name="T">The type of the data being returned.</typeparam>
    /// <param name="data">The data to include in the response.</param>
    /// <returns>An <see cref="IActionResult"/> containing the successful API response.</returns>
    protected IActionResult OkResponse<T>(T data)
    {
        return Ok(APIResponse<T>.Success(data));
    }
}

