using FluentAssertions;
using Microsoft.Extensions.Options;
using Moq;
using WarrantyBee.Application.Abstractions.Persistence;
using WarrantyBee.Application.Abstractions.Services;
using WarrantyBee.Application.Configuration;
using WarrantyBee.Application.Contracts.Identity;
using WarrantyBee.Application.Contracts.Users;
using WarrantyBee.Application.Services;
using WarrantyBee.Domain.Enums;
using WarrantyBee.Domain.Exceptions;
using Xunit;

namespace WarrantyBee.Tests.Services;

public class AuthServiceTests
{
    private readonly Mock<IUserRepository> _userRepositoryMock;
    private readonly Mock<IOtpRepository> _otpRepositoryMock;
    private readonly Mock<IOtpService> _otpServiceMock;
    private readonly Mock<ICaptchaService> _captchaServiceMock;
    private readonly Mock<ITokenService> _tokenServiceMock;
    private readonly Mock<IEmailService> _emailServiceMock;
    private readonly Mock<ITelemetryService> _telemetryServiceMock;
    private readonly Mock<ICacheService> _cacheServiceMock;
    private readonly Mock<IOptions<AppConfiguration>> _configMock;
    private readonly AuthService _service;

    public AuthServiceTests()
    {
        _userRepositoryMock = new Mock<IUserRepository>();
        _otpRepositoryMock = new Mock<IOtpRepository>();
        _otpServiceMock = new Mock<IOtpService>();
        _captchaServiceMock = new Mock<ICaptchaService>();
        _tokenServiceMock = new Mock<ITokenService>();
        _emailServiceMock = new Mock<IEmailService>();
        _telemetryServiceMock = new Mock<ITelemetryService>();
        _cacheServiceMock = new Mock<ICacheService>();
        _configMock = new Mock<IOptions<AppConfiguration>>();
        
        _configMock.Setup(c => c.Value).Returns(new AppConfiguration());

        _service = new AuthService(
            _configMock.Object,
            _tokenServiceMock.Object,
            _cacheServiceMock.Object,
            _captchaServiceMock.Object,
            _otpServiceMock.Object,
            _emailServiceMock.Object,
            _telemetryServiceMock.Object,
            _userRepositoryMock.Object,
            _otpRepositoryMock.Object);
    }

    [Fact]
    public async Task LoginAsync_InvalidCaptcha_ThrowsApiException()
    {
        // Arrange
        var request = new SimpleLoginRequest { Email = "test@example.com", CaptchaResponse = "wrong" };
        _captchaServiceMock.Setup(s => s.ValidateAsync("wrong")).ReturnsAsync(false);

        // Act
        var act = () => _service.LoginAsync(request);

        // Assert
        await act.Should().ThrowAsync<ApiException>().Where(e => e.Error == Errors.InvalidCaptcha);
    }

    [Fact]
    public async Task LoginAsync_UserNotFound_ThrowsApiException()
    {
        // Arrange
        var request = new SimpleLoginRequest { Email = "none@example.com", CaptchaResponse = "ok" };
        _captchaServiceMock.Setup(s => s.ValidateAsync("ok")).ReturnsAsync(true);
        _userRepositoryMock.Setup(r => r.GetAsync(It.IsAny<UserSearchFilter>())).ReturnsAsync((UserResponse?)null);

        // Act
        var act = () => _service.LoginAsync(request);

        // Assert
        await act.Should().ThrowAsync<ApiException>().Where(e => e.Error == Errors.UserNotRegistered);
    }

    [Fact]
    public async Task SignUpAsync_UserAlreadyExists_ThrowsApiException()
    {
        // Arrange
        var request = new SignUpRequest 
        { 
            Email = "exists@example.com", 
            HasAcceptedTermsAndConditions = true, 
            HasAcceptedPrivacyPolicy = true,
            Firstname = "Test",
            Lastname = "User",
            Password = "StrongPassword123!",
            AuthProvider = (byte)AuthProvider.Internal,
            CaptchaResponse = "valid"
        };
        _captchaServiceMock.Setup(s => s.ValidateAsync("valid")).ReturnsAsync(true);
        _userRepositoryMock.Setup(r => r.GetAsync(It.IsAny<UserSearchFilter>())).ReturnsAsync(new UserResponse());

        // Act
        var act = () => _service.SignUpAsync(request);

        // Assert
        await act.Should().ThrowAsync<ApiException>().Where(e => e.Error == Errors.UserAlreadyRegistered);
    }
}
