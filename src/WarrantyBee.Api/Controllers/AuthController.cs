using Microsoft.AspNetCore.Mvc;
using WarrantyBee.Application.Abstractions.Services;
using WarrantyBee.Application.Contracts.Identity;

namespace WarrantyBee.Api.Controllers;

[Route("api/[controller]")]
public class AuthController : BaseController
{
    private readonly IAuthService _authService;

    public AuthController(IAuthService authService)
    {
        _authService = authService;
    }

    [HttpPost("login")]
    public async Task<IActionResult> Login([FromBody] SimpleLoginRequest request)
    {
        var result = await _authService.LoginAsync(request);
        return OkResponse(result);
    }
    
    // In Java, polymorphism was handled by @JsonTypeInfo on the base LoginRequest.
    // In C#, we can do the same with JsonDerivedType, but for simplicity here I'll just use one endpoint
    // or separate them. Given the user's Java code, I'll use the base class and rely on JsonDerivedType.
    
    [HttpPost("login/mfa")]
    public async Task<IActionResult> MfaLogin([FromBody] MFALoginRequest request)
    {
        var result = await _authService.LoginAsync(request);
        return OkResponse(result);
    }

    [HttpPost("signup")]
    public async Task<IActionResult> SignUp([FromBody] SignUpRequest request)
    {
        var result = await _authService.SignUpAsync(request);
        return OkResponse(result);
    }

    [HttpPost("forgotpassword")]
    public async Task<IActionResult> ForgotPassword([FromBody] ForgotPasswordRequest request)
    {
        await _authService.ForgotPasswordAsync(request);
        return OkResponse<object?>(null);
    }

    [HttpPost("resetpassword")]
    public async Task<IActionResult> ResetPassword([FromBody] ResetPasswordRequest request)
    {
        await _authService.ResetPasswordAsync(request);
        return OkResponse<object?>(null);
    }
}
