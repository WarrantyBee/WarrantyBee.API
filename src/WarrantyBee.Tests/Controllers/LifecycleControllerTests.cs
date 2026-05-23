using FluentAssertions;
using Microsoft.AspNetCore.Mvc;
using WarrantyBee.Api.Controllers;
using WarrantyBee.Shared.Core.Contracts;
using Xunit;

namespace WarrantyBee.Tests.Controllers;

public class LifecycleControllerTests
{
    private readonly LifecycleController _controller;

    public LifecycleControllerTests()
    {
        _controller = new LifecycleController();
    }

    [Fact]
    public void Alive_ReturnsOkResponse()
    {
        // Act
        var result = _controller.Alive();

        // Assert
        var okResult = result.As<OkObjectResult>();
        okResult.Should().NotBeNull();
        
        var response = okResult.Value.As<APIResponse<object?>>();
        response.Should().NotBeNull();
        response.Error.Should().BeNull();
    }
}

