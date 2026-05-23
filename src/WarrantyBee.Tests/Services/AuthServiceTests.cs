using FluentAssertions;
using Microsoft.Extensions.Options;
using Moq;
using WarrantyBee.Application.Abstractions.Persistence;
using WarrantyBee.Application.Abstractions.Services;
using WarrantyBee.Application.Common;
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
    private readonly Mock<ITelemetryService> _telemetryServiceMock;
    private readonly Mock<ICacheService> _cacheServiceMock;
    private readonly Mock<IJobSchedulerClient> _jobSchedulerMock;
    private readonly Mock<IEventPublisher> _eventPublisherMock;
    private readonly Mock<IOptions<AppConfiguration>> _configMock;
    private readonly AuthService _service;

    public AuthServiceTests()
    {
        _userRepositoryMock = new Mock<IUserRepository>();
        _otpRepositoryMock = new Mock<IOtpRepository>();
        _otpServiceMock = new Mock<IOtpService>();
        _captchaServiceMock = new Mock<ICaptchaService>();
        _tokenServiceMock = new Mock<ITokenService>();
        _telemetryServiceMock = new Mock<ITelemetryService>();
        _cacheServiceMock = new Mock<ICacheService>();
        _jobSchedulerMock = new Mock<IJobSchedulerClient>();
        _eventPublisherMock = new Mock<IEventPublisher>();
        _configMock = new Mock<IOptions<AppConfiguration>>();
        
        var config = new AppConfiguration { Profile = new ProfileConfiguration { PasswordResetWindow = 60 } };
        _configMock.Setup(c => c.Value).Returns(config);

        _service = new AuthService(
            _configMock.Object,
            _tokenServiceMock.Object,
            _cacheServiceMock.Object,
            _captchaServiceMock.Object,
            _otpServiceMock.Object,
            _telemetryServiceMock.Object,
            _userRepositoryMock.Object,
            _otpRepositoryMock.Object,
            _jobSchedulerMock.Object,
            _eventPublisherMock.Object);
    }

    #region Login Tests

    [Fact]
    public async Task LoginAsync_InvalidCaptcha_ThrowsApiException()
    {
        var request = new SimpleLoginRequest { Email = "test@example.com", CaptchaResponse = "wrong" };
        _captchaServiceMock.Setup(s => s.ValidateAsync("wrong")).ReturnsAsync(false);
        var act = () => _service.LoginAsync(request);
        await act.Should().ThrowAsync<ApiException>().Where(e => e.Error == Errors.InvalidCaptcha);
    }

    [Fact]
    public async Task LoginAsync_Simple_UserNotRegistered_ThrowsApiException()
    {
        var request = new SimpleLoginRequest { Email = "none@example.com", CaptchaResponse = "ok", Password = "Password123!" };
        _captchaServiceMock.Setup(s => s.ValidateAsync("ok")).ReturnsAsync(true);
        _userRepositoryMock.Setup(r => r.GetAsync(It.IsAny<UserSearchFilter>())).ReturnsAsync((UserResponse?)null);
        var act = () => _service.LoginAsync(request);
        await act.Should().ThrowAsync<ApiException>().Where(e => e.Error == Errors.UserNotRegistered);
    }

    [Fact]
    public async Task LoginAsync_Simple_MfaEnabled_ReturnsMfaResponse()
    {
        var password = "Password123!";
        var passwordHash = HashHelper.GetHash(password);
        var request = new SimpleLoginRequest { Email = "mfa@example.com", CaptchaResponse = "ok", Password = password, AuthProvider = (byte)AuthProvider.Internal };
        var user = new UserResponse 
        { 
            Id = 1, Email = "mfa@example.com", Password = passwordHash, AuthProvider = (byte)AuthProvider.Internal,
            Profile = new UserProfileResponse { Settings = new UserSettingsResponse { Is2FAEnabled = true } } 
        };
        
        _captchaServiceMock.Setup(s => s.ValidateAsync("ok")).ReturnsAsync(true);
        _userRepositoryMock.Setup(r => r.GetAsync(It.IsAny<UserSearchFilter>())).ReturnsAsync(user);
        _userRepositoryMock.Setup(r => r.StoreTokenAsync(It.IsAny<LoginTokenDetails>())).ReturnsAsync(true);
        _otpServiceMock.Setup(s => s.Generate()).Returns("123456");

        var result = await _service.LoginAsync(request);
        result.Should().BeOfType<MFALoginResponse>();
    }

    [Fact]
    public async Task LoginAsync_Simple_TokenSaveFailure_ThrowsApiException()
    {
        var password = "Password123!";
        var passwordHash = HashHelper.GetHash(password);
        var request = new SimpleLoginRequest { Email = "mfa@example.com", CaptchaResponse = "ok", Password = password, AuthProvider = (byte)AuthProvider.Internal };
        var user = new UserResponse 
        { 
            Id = 1, Email = "mfa@example.com", Password = passwordHash, AuthProvider = (byte)AuthProvider.Internal,
            Profile = new UserProfileResponse { Settings = new UserSettingsResponse { Is2FAEnabled = true } } 
        };
        
        _captchaServiceMock.Setup(s => s.ValidateAsync("ok")).ReturnsAsync(true);
        _userRepositoryMock.Setup(r => r.GetAsync(It.IsAny<UserSearchFilter>())).ReturnsAsync(user);
        _userRepositoryMock.Setup(r => r.StoreTokenAsync(It.IsAny<LoginTokenDetails>())).ReturnsAsync(false);

        var act = () => _service.LoginAsync(request);
        await act.Should().ThrowAsync<ApiException>().Where(e => e.Error == Errors.LoginTokenCouldNotBeSaved);
    }

    [Fact]
    public async Task LoginAsync_Mfa_Success_ReturnsLoginResponse()
    {
        var otp = "123456";
        var otpHash = HashHelper.GetHash(otp);
        var request = new MFALoginRequest { Email = "mfa@example.com", Token = "token", Otp = otp, CaptchaResponse = "ok" };
        var user = new UserResponse { Id = 1, Email = "mfa@example.com", Profile = new UserProfileResponse { Settings = new UserSettingsResponse { Is2FAEnabled = true } } };
        
        _captchaServiceMock.Setup(s => s.ValidateAsync("ok")).ReturnsAsync(true);
        _userRepositoryMock.Setup(r => r.GetAsync(It.IsAny<UserSearchFilter>())).ReturnsAsync(user);
        _userRepositoryMock.Setup(r => r.ValidateTokenAsync(It.IsAny<LoginTokenDetails>())).ReturnsAsync(true);
        _otpRepositoryMock.Setup(r => r.GetAsync(It.IsAny<OtpSearchFilter>())).ReturnsAsync(otpHash);
        _cacheServiceMock.Setup(s => s.GetAsync(It.IsAny<string>())).ReturnsAsync("cached_token");
        _tokenServiceMock.Setup(s => s.Validate("cached_token")).Returns(new Dictionary<string, object> { ["iat"] = 0, ["exp"] = 0 });
        
        var result = await _service.LoginAsync(request);
        result.Should().BeOfType<LoginResponse>();
    }

    [Fact]
    public async Task LoginAsync_Mfa_NotEnabled_ThrowsApiException()
    {
        var request = new MFALoginRequest { Email = "test@example.com", CaptchaResponse = "ok", Token = "token", Otp = "123456" };
        var user = new UserResponse { Id = 1, Profile = new UserProfileResponse { Settings = new UserSettingsResponse { Is2FAEnabled = false } } };
        
        _captchaServiceMock.Setup(s => s.ValidateAsync("ok")).ReturnsAsync(true);
        _userRepositoryMock.Setup(r => r.GetAsync(It.IsAny<UserSearchFilter>())).ReturnsAsync(user);

        var act = () => _service.LoginAsync(request);
        await act.Should().ThrowAsync<ApiException>().Where(e => e.Error == Errors.MfaNotEnabled);
    }

    #endregion

    #region SignUp Tests

    [Fact]
    public async Task SignUpAsync_UserCreationFailure_ThrowsApiException()
    {
        var request = new SignUpRequest 
        { 
            Email = "fail@example.com", HasAcceptedTermsAndConditions = true, HasAcceptedPrivacyPolicy = true,
            Firstname = "John", Lastname = "Doe", Password = "StrongPassword123!",
            AuthProvider = (byte)AuthProvider.Internal, CaptchaResponse = "valid"
        };
        _captchaServiceMock.Setup(s => s.ValidateAsync("valid")).ReturnsAsync(true);
        _userRepositoryMock.Setup(r => r.GetAsync(It.IsAny<UserSearchFilter>())).ReturnsAsync((UserResponse?)null);
        _userRepositoryMock.Setup(r => r.CreateAsync(request)).ReturnsAsync(0L);
        var act = () => _service.SignUpAsync(request);
        await act.Should().ThrowAsync<ApiException>().Where(e => e.Error == Errors.UserRegistrationFailed);
    }

    [Fact]
    public async Task SignUpAsync_Success_TriggersWelcomeEmail()
    {
        var request = new SignUpRequest 
        { 
            Email = "new@example.com", HasAcceptedTermsAndConditions = true, HasAcceptedPrivacyPolicy = true,
            Firstname = "John", Lastname = "Doe", Password = "StrongPassword123!",
            AuthProvider = (byte)AuthProvider.Internal, CaptchaResponse = "valid"
        };
        _captchaServiceMock.Setup(s => s.ValidateAsync("valid")).ReturnsAsync(true);
        _userRepositoryMock.Setup(r => r.GetAsync(It.IsAny<UserSearchFilter>())).ReturnsAsync((UserResponse?)null);
        _userRepositoryMock.Setup(r => r.CreateAsync(request)).ReturnsAsync(1L);
        var result = await _service.SignUpAsync(request);
        result.Id.Should().Be(1L);
        
        _eventPublisherMock.Verify(s => s.PublishAsync("user.signup", It.IsAny<object>()), Times.Once);
        _jobSchedulerMock.Verify(s => s.EnqueueNotificationAsync(1L, NotificationType.WelcomeEmail, null), Times.Once);
    }

    #endregion

    #region Forgot Password Tests

    [Fact]
    public async Task ForgotPasswordAsync_RecentlyUpdated_ThrowsApiException()
    {
        var user = new UserResponse { Email = "recent@example.com", Profile = new UserProfileResponse { Settings = new UserSettingsResponse { PasswordUpdatedAt = DateTime.UtcNow.AddMinutes(-5) } } };
        var request = new ForgotPasswordRequest { Email = "recent@example.com", CaptchaResponse = "ok" };
        _captchaServiceMock.Setup(s => s.ValidateAsync("ok")).ReturnsAsync(true);
        _userRepositoryMock.Setup(r => r.GetAsync(It.IsAny<UserSearchFilter>())).ReturnsAsync(user);
        var act = () => _service.ForgotPasswordAsync(request);
        await act.Should().ThrowAsync<ApiException>().Where(e => e.Error == Errors.PasswordRecentlyUpdated);
    }

    #endregion

    #region Reset Password Tests

    [Fact]
    public async Task ResetPasswordAsync_PasswordAlreadyUsed_ThrowsApiException()
    {
        var request = new ResetPasswordRequest { Email = "test@example.com", Otp = "123456", NewPassword = "OldPassword123!", CaptchaResponse = "ok" };
        var user = new UserResponse { Id = 1, Email = "test@example.com" };
        var otpHash = HashHelper.GetHash("123456");
        _captchaServiceMock.Setup(s => s.ValidateAsync("ok")).ReturnsAsync(true);
        _userRepositoryMock.Setup(r => r.GetAsync(It.IsAny<UserSearchFilter>())).ReturnsAsync(user);
        _otpRepositoryMock.Setup(r => r.GetAsync(It.IsAny<OtpSearchFilter>())).ReturnsAsync(otpHash);
        _userRepositoryMock.Setup(r => r.GetPasswordsAsync(1L)).ReturnsAsync(new List<string> { HashHelper.GetHash("OldPassword123!") });
        var act = () => _service.ResetPasswordAsync(request);
        await act.Should().ThrowAsync<ApiException>().Where(e => e.Error == Errors.PasswordAlreadyUsed);
    }

    #endregion
}
