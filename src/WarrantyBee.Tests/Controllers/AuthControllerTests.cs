using FluentAssertions;
using Microsoft.AspNetCore.Mvc;
using Moq;
using WarrantyBee.Api.Controllers;
using WarrantyBee.Application.Abstractions.Services;
using WarrantyBee.Application.Contracts.Common;
using WarrantyBee.Application.Contracts.Identity;
using WarrantyBee.Application.Contracts.Users;
using Xunit;

namespace WarrantyBee.Tests.Controllers;

public class AuthControllerTests
{
    private readonly Mock<IAuthService> _authServiceMock;
    private readonly AuthController _controller;

    public AuthControllerTests()
    {
        _authServiceMock = new Mock<IAuthService>();
        _controller = new AuthController(_authServiceMock.Object);
    }

    [Fact]
    public async Task Login_Simple_Success()
    {
        var request = new SimpleLoginRequest { Email = "test@example.com" };
        var expectedResponse = new LoginResponse("token", "iat", "exp", new UserResponse());
        _authServiceMock.Setup(s => s.LoginAsync(request)).ReturnsAsync(expectedResponse);

        var result = await _controller.Login(request);

        var okResult = result.As<OkObjectResult>();
        okResult.Value.As<APIResponse<ILoginResponse>>().Data.Should().Be(expectedResponse);
    }

    [Fact]
    public async Task MfaLogin_Success()
    {
        var request = new MFALoginRequest { Email = "test@example.com" };
        var expectedResponse = new LoginResponse("token", "iat", "exp", new UserResponse());
        _authServiceMock.Setup(s => s.LoginAsync(request)).ReturnsAsync(expectedResponse);

        var result = await _controller.MfaLogin(request);

        var okResult = result.As<OkObjectResult>();
        okResult.Value.As<APIResponse<ILoginResponse>>().Data.Should().Be(expectedResponse);
    }

    [Fact]
    public async Task SignUp_Success()
    {
        var request = new SignUpRequest { Email = "test@example.com" };
        var expectedResponse = new SignUpResponse(1L);
        _authServiceMock.Setup(s => s.SignUpAsync(request)).ReturnsAsync(expectedResponse);

        var result = await _controller.SignUp(request);

        var okResult = result.As<OkObjectResult>();
        okResult.Value.As<APIResponse<SignUpResponse>>().Data.Should().Be(expectedResponse);
    }

    [Fact]
    public async Task ForgotPassword_Success()
    {
        var request = new ForgotPasswordRequest { Email = "test@example.com" };
        var result = await _controller.ForgotPassword(request);
        result.Should().BeOfType<OkObjectResult>();
    }

    [Fact]
    public async Task ResetPassword_Success()
    {
        var request = new ResetPasswordRequest { Email = "test@example.com" };
        var result = await _controller.ResetPassword(request);
        result.Should().BeOfType<OkObjectResult>();
    }
}
