using FluentAssertions;
using Moq;
using WarrantyBee.Application.Abstractions.Persistence;
using WarrantyBee.Application.Abstractions.Services;
using WarrantyBee.Application.Contracts.Users;
using WarrantyBee.Application.Services;
using WarrantyBee.Domain.Enums;
using WarrantyBee.Domain.Exceptions;
using Xunit;

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

    [Fact]
    public async Task GetAsync_UserNotAuthenticated_ThrowsApiException()
    {
        // Arrange
        _userContextMock.Setup(c => c.UserId).Returns((long?)null);

        // Act
        var act = () => _service.GetAsync();

        // Assert
        await act.Should().ThrowAsync<ApiException>().Where(e => e.Error == Errors.Unauthenticated);
    }

    [Fact]
    public async Task GetAsync_UserAuthenticated_ReturnsUser()
    {
        // Arrange
        var userId = 1L;
        var expectedUser = new UserResponse { Id = userId };
        _userContextMock.Setup(c => c.UserId).Returns(userId);
        _userRepositoryMock.Setup(r => r.GetAsync(It.Is<UserSearchFilter>(f => f.Id == userId)))
            .ReturnsAsync(expectedUser);

        // Act
        var result = await _service.GetAsync();

        // Assert
        result.Should().Be(expectedUser);
    }

    [Fact]
    public async Task UpdateProfileAsync_Success()
    {
        // Arrange
        var userId = 1L;
        var request = new ProfileUpdateRequest { AddressLine1 = "123 St" };
        _userContextMock.Setup(c => c.UserId).Returns(userId);
        _userRepositoryMock.Setup(r => r.UpdateProfileAsync(request)).ReturnsAsync(true);

        // Act
        await _service.UpdateProfileAsync(request);

        // Assert
        request.UserId.Should().Be(userId);
        _userRepositoryMock.Verify(r => r.UpdateProfileAsync(request), Times.Once);
    }
}
