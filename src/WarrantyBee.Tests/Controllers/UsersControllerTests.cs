using FluentAssertions;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Moq;
using WarrantyBee.Api.Controllers;
using WarrantyBee.Application.Abstractions.Persistence;
using WarrantyBee.Application.Abstractions.Services;
using WarrantyBee.Application.Contracts.Common;
using WarrantyBee.Application.Contracts.Users;
using Xunit;

namespace WarrantyBee.Tests.Controllers;

public class UsersControllerTests
{
    private readonly Mock<IUserService> _userServiceMock;
    private readonly UsersController _controller;

    public UsersControllerTests()
    {
        _userServiceMock = new Mock<IUserService>();
        _controller = new UsersController(_userServiceMock.Object);
    }

    [Fact]
    public async Task Get_ReturnsOkResponse_WithUserResponse()
    {
        // Arrange
        var expectedUser = new UserResponse { Id = 1, Email = "test@example.com" };
        _userServiceMock.Setup(s => s.GetAsync()).ReturnsAsync(expectedUser);

        // Act
        var result = await _controller.Get();

        // Assert
        var okResult = result.As<OkObjectResult>();
        okResult.Should().NotBeNull();
        
        var response = okResult.Value.As<APIResponse<UserResponse>>();
        response.Should().NotBeNull();
        response.Data.Should().Be(expectedUser);
    }

    [Fact]
    public async Task UpdateProfile_ReturnsOkResponse()
    {
        // Arrange
        var request = new ProfileUpdateRequest { AddressLine1 = "Updated" };

        // Act
        var result = await _controller.UpdateProfile(request);

        // Assert
        var okResult = result.As<OkObjectResult>();
        okResult.Should().NotBeNull();
        _userServiceMock.Verify(s => s.UpdateProfileAsync(request), Times.Once);
    }

    [Fact]
    public async Task ChangeAvatar_ReturnsOkResponse_WithAvatarResponse()
    {
        // Arrange
        var fileMock = new Mock<IFormFile>();
        var content = "fake content";
        var fileName = "test.png";
        var ms = new MemoryStream();
        var writer = new StreamWriter(ms);
        writer.Write(content);
        writer.Flush();
        ms.Position = 0;
        fileMock.Setup(_ => _.OpenReadStream()).Returns(ms);
        fileMock.Setup(_ => _.FileName).Returns(fileName);
        fileMock.Setup(_ => _.ContentType).Returns("image/png");

        var expectedResponse = new AvatarResponse("http://example.com/avatar.png");
        _userServiceMock.Setup(s => s.ChangeAvatarAsync(0, It.IsAny<Stream>(), fileName, "image/png"))
            .ReturnsAsync(expectedResponse);

        // Act
        var result = await _controller.ChangeAvatar(fileMock.Object, "captcha");

        // Assert
        var okResult = result.As<OkObjectResult>();
        okResult.Should().NotBeNull();
        
        var response = okResult.Value.As<APIResponse<AvatarResponse>>();
        response.Should().NotBeNull();
        response.Data.Should().Be(expectedResponse);
    }
}
