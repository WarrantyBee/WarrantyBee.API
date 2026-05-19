using Microsoft.AspNetCore.Mvc;
using WarrantyBee.Application.Contracts.Common;

namespace WarrantyBee.Api.Controllers;

[ApiController]
public abstract class BaseController : ControllerBase
{
    protected IActionResult OkResponse<T>(T data)
    {
        return Ok(APIResponse<T>.Success(data));
    }
}
