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
    public async Task Login_ReturnsOkResponse_WithLoginResponse()
    {
        // Arrange
        var request = new SimpleLoginRequest { Email = "test@example.com", Password = "password" };
        var expectedResponse = new LoginResponse("token", "issued", "expires", new UserResponse { Id = 1 });
        _authServiceMock.Setup(s => s.LoginAsync(request)).ReturnsAsync(expectedResponse);

        // Act
        var result = await _controller.Login(request);

        // Assert
        var okResult = result.As<OkObjectResult>();
        okResult.Should().NotBeNull();
        
        var response = okResult.Value.As<APIResponse<ILoginResponse>>();
        response.Should().NotBeNull();
        response.Data.Should().Be(expectedResponse);
    }

    [Fact]
    public async Task SignUp_ReturnsOkResponse_WithSignUpResponse()
    {
        // Arrange
        var request = new SignUpRequest { Email = "test@example.com", Firstname = "Test", Lastname = "User" };
        var expectedResponse = new SignUpResponse(1);
        _authServiceMock.Setup(s => s.SignUpAsync(request)).ReturnsAsync(expectedResponse);

        // Act
        var result = await _controller.SignUp(request);

        // Assert
        var okResult = result.As<OkObjectResult>();
        okResult.Should().NotBeNull();
        
        var response = okResult.Value.As<APIResponse<SignUpResponse>>();
        response.Should().NotBeNull();
        response.Data.Should().Be(expectedResponse);
    }

    [Fact]
    public async Task ForgotPassword_ReturnsOkResponse()
    {
        // Arrange
        var request = new ForgotPasswordRequest { Email = "test@example.com" };

        // Act
        var result = await _controller.ForgotPassword(request);

        // Assert
        var okResult = result.As<OkObjectResult>();
        okResult.Should().NotBeNull();
        _authServiceMock.Verify(s => s.ForgotPasswordAsync(request), Times.Once);
    }

    [Fact]
    public async Task ResetPassword_ReturnsOkResponse()
    {
        // Arrange
        var request = new ResetPasswordRequest { Email = "test@example.com", Otp = "123456", NewPassword = "newpassword" };

        // Act
        var result = await _controller.ResetPassword(request);

        // Assert
        var okResult = result.As<OkObjectResult>();
        okResult.Should().NotBeNull();
        _authServiceMock.Verify(s => s.ResetPasswordAsync(request), Times.Once);
    }
}
