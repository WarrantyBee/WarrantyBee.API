using FluentAssertions;
using Moq;
using WarrantyBee.Application.Abstractions.Persistence;
using WarrantyBee.Shared.Infrastructure.Abstractions;
using WarrantyBee.Application.Contracts.Users;
using WarrantyBee.Application.Services;
using WarrantyBee.Shared.Core.Enums;
using WarrantyBee.Shared.Core.Exceptions;
using Xunit;
using WarrantyBee.Application.Abstractions.Services;

namespace WarrantyBee.Tests.Services;

public class UserServiceTests
{
    private readonly Mock<IUserRepository> _userRepositoryMock;
    private readonly Mock<ICurrentUserContext> _userContextMock;
    private readonly Mock<IStorageService> _storageServiceMock;
    private readonly Mock<ICaptchaService> _captchaServiceMock;
    private readonly UserService _service;

    public UserServiceTests()
    {
        _userRepositoryMock = new Mock<IUserRepository>();
        _userContextMock = new Mock<ICurrentUserContext>();
        _storageServiceMock = new Mock<IStorageService>();
        _captchaServiceMock = new Mock<ICaptchaService>();

        _service = new UserService(
            _userContextMock.Object,
            _storageServiceMock.Object,
            _captchaServiceMock.Object,
            _userRepositoryMock.Object);
    }

    #region GetAsync Tests

    [Fact]
    public async Task GetAsync_UserNotAuthenticated_ThrowsApiException()
    {
        _userContextMock.Setup(c => c.UserId).Returns((long?)null);
        var act = () => _service.GetAsync();
        await act.Should().ThrowAsync<ApiException>().Where(e => e.Error == Errors.Unauthenticated);
    }

    [Fact]
    public async Task GetAsync_UserNotFound_ThrowsApiException()
    {
        _userContextMock.Setup(c => c.UserId).Returns(1L);
        _userRepositoryMock.Setup(r => r.GetAsync(It.IsAny<UserSearchFilter>())).ReturnsAsync((UserResponse?)null);
        var act = () => _service.GetAsync();
        await act.Should().ThrowAsync<ApiException>().Where(e => e.Error == Errors.UserNotFound);
    }

    #endregion

    #region ChangeAvatarAsync Tests

    [Fact]
    public async Task ChangeAvatarAsync_FileIsEmpty_ThrowsApiException()
    {
        var ms = new MemoryStream();
        var act = () => _service.ChangeAvatarAsync(1L, ms, "test.png", "image/png");
        await act.Should().ThrowAsync<ApiException>().Where(e => e.Error == Errors.FileIsEmpty);
    }

    [Fact]
    public async Task ChangeAvatarAsync_FileExceededSize_ThrowsApiException()
    {
        var ms = new MemoryStream(new byte[3 * 1024 * 1024]); // 3MB
        var act = () => _service.ChangeAvatarAsync(1L, ms, "test.png", "image/png");
        await act.Should().ThrowAsync<ApiException>().Where(e => e.Error == Errors.FileExceededAllowedSize);
    }

    [Fact]
    public async Task ChangeAvatarAsync_StorageServiceError_ThrowsApiException()
    {
        var ms = new MemoryStream(new byte[100]);
        var user = new UserResponse { Id = 1, Profile = new UserProfileResponse() };
        _userContextMock.Setup(c => c.UserId).Returns(1L);
        _userRepositoryMock.Setup(r => r.GetAsync(It.IsAny<UserSearchFilter>())).ReturnsAsync(user);
        _storageServiceMock.Setup(s => s.UploadAsync(ms, "new.png", "image/png")).ReturnsAsync(string.Empty);

        var act = () => _service.ChangeAvatarAsync(0, ms, "new.png", "image/png");
        await act.Should().ThrowAsync<ApiException>().Where(e => e.Error == Errors.StorageServiceError);
    }

    [Fact]
    public async Task ChangeAvatarAsync_AvatarUpdateFailure_ThrowsApiException()
    {
        var ms = new MemoryStream(new byte[100]);
        var user = new UserResponse { Id = 1, Profile = new UserProfileResponse() };
        _userContextMock.Setup(c => c.UserId).Returns(1L);
        _userRepositoryMock.Setup(r => r.GetAsync(It.IsAny<UserSearchFilter>())).ReturnsAsync(user);
        _storageServiceMock.Setup(s => s.UploadAsync(ms, "new.png", "image/png")).ReturnsAsync("new_url.png");
        _userRepositoryMock.Setup(r => r.UpdateProfileAsync(It.IsAny<ProfileUpdateRequest>())).ReturnsAsync(false);

        var act = () => _service.ChangeAvatarAsync(0, ms, "new.png", "image/png");
        await act.Should().ThrowAsync<ApiException>().Where(e => e.Error == Errors.AvatarCouldNotBeUpdated);
        _storageServiceMock.Verify(s => s.DeleteByUrlAsync("new_url.png"), Times.Once);
    }

    #endregion

    #region UpdateProfileAsync Tests

    [Fact]
    public async Task UpdateProfileAsync_InvalidPostalCode_ThrowsApiException()
    {
        var request = new ProfileUpdateRequest { PostalCode = "ABC" };
        _userContextMock.Setup(c => c.UserId).Returns(1L);
        var act = () => _service.UpdateProfileAsync(request);
        await act.Should().ThrowAsync<ApiException>().Where(e => e.Error == Errors.InvalidPostalCode);
    }

    [Fact]
    public async Task UpdateProfileAsync_ProfileUpdateFailure_ThrowsApiException()
    {
        var request = new ProfileUpdateRequest { AddressLine1 = "123 Main St", City = "New York", PhoneCode = "+1", PhoneNumber = "1234567890" };
        _userContextMock.Setup(c => c.UserId).Returns(1L);
        _userRepositoryMock.Setup(r => r.UpdateProfileAsync(request)).ReturnsAsync(false);

        var act = () => _service.UpdateProfileAsync(request);
        await act.Should().ThrowAsync<ApiException>().Where(e => e.Error == Errors.ProfileCouldNotBeUpdated);
    }

    #endregion
}
