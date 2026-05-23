using Microsoft.AspNetCore.Mvc;
using WarrantyBee.Shared.Infrastructure.Abstractions;
using WarrantyBee.Application.Contracts.Identity;
using WarrantyBee.Application.Abstractions.Services;

namespace WarrantyBee.Api.Controllers;

/// <summary>
/// Controller responsible for handling authentication-related requests such as login, signup, and password recovery.
/// </summary>
[Route("api/[controller]")]
public class AuthController : BaseController
{
    private readonly IAuthService _authService;

    /// <summary>
    /// Initializes a new instance of the <see cref="AuthController"/> class.
    /// </summary>
    /// <param name="authService">The service used for authentication logic.</param>
    public AuthController(IAuthService authService)
    {
        _authService = authService;
    }

    /// <summary>
    /// Authenticates a user using simple credentials (email and password).
    /// </summary>
    /// <param name="request">The login request containing user credentials.</param>
    /// <returns>A response containing the login result, which may include a JWT or an MFA token requirement.</returns>
    [HttpPost("login")]
    public async Task<IActionResult> Login([FromBody] SimpleLoginRequest request)
    {
        var result = await _authService.LoginAsync(request);
        return OkResponse(result);
    }
    
    /// <summary>
    /// Authenticates a user using a multi-factor authentication (MFA) token.
    /// </summary>
    /// <param name="request">The MFA login request containing the MFA token or OTP.</param>
    /// <returns>A response containing the final login result (JWT).</returns>
    [HttpPost("login/mfa")]
    public async Task<IActionResult> MfaLogin([FromBody] MFALoginRequest request)
    {
        var result = await _authService.LoginAsync(request);
        return OkResponse(result);
    }

    /// <summary>
    /// Registers a new user in the system.
    /// </summary>
    /// <param name="request">The signup request containing user profile information.</param>
    /// <returns>A response containing the ID of the newly created user.</returns>
    [HttpPost("signup")]
    public async Task<IActionResult> SignUp([FromBody] SignUpRequest request)
    {
        var result = await _authService.SignUpAsync(request);
        return OkResponse(result);
    }

    /// <summary>
    /// Initiates the forgot password process by sending an OTP to the user's email.
    /// </summary>
    /// <param name="request">The request containing the user's email address.</param>
    /// <returns>A successful status response if the process is initiated.</returns>
    [HttpPost("forgotpassword")]
    public async Task<IActionResult> ForgotPassword([FromBody] ForgotPasswordRequest request)
    {
        await _authService.ForgotPasswordAsync(request);
        return OkResponse<object?>(null);
    }

    /// <summary>
    /// Resets the user's password using a valid OTP.
    /// </summary>
    /// <param name="request">The request containing the OTP, email, and the new password.</param>
    /// <returns>A successful status response if the password is reset successfully.</returns>
    [HttpPost("resetpassword")]
    public async Task<IActionResult> ResetPassword([FromBody] ResetPasswordRequest request)
    {
        await _authService.ResetPasswordAsync(request);
        return OkResponse<object?>(null);
    }
}
