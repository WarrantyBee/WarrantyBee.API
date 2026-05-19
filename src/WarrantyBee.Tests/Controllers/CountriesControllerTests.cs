using FluentAssertions;
using Microsoft.AspNetCore.Mvc;
using Moq;
using WarrantyBee.Api.Controllers;
using WarrantyBee.Application.Abstractions.Persistence;
using WarrantyBee.Application.Abstractions.Services;
using WarrantyBee.Application.Contracts.Common;
using WarrantyBee.Application.Contracts.Geographic;
using Xunit;

namespace WarrantyBee.Tests.Controllers;

public class CountriesControllerTests
{
    private readonly Mock<ICountryService> _countryServiceMock;
    private readonly CountriesController _controller;

    public CountriesControllerTests()
    {
        _countryServiceMock = new Mock<ICountryService>();
        _controller = new CountriesController(_countryServiceMock.Object);
    }

    [Fact]
    public async Task Get_ReturnsOkResponse_WithCountries()
    {
        // Arrange
        var expectedCountries = new List<CountryDetailResponse>
        {
            new CountryDetailResponse(1, "United States", "US", "USA", "1", "United States of America", "Washington D.C.", "1")
        };
        _countryServiceMock.Setup(s => s.GetAsync()).ReturnsAsync(expectedCountries);

        // Act
        var result = await _controller.Get();

        // Assert
        var okResult = result.As<OkObjectResult>();
        okResult.Should().NotBeNull();
        
        var response = okResult.Value.As<APIResponse<IEnumerable<CountryDetailResponse>>>();
        response.Should().NotBeNull();
        response.Data.Should().BeEquivalentTo(expectedCountries);
    }
}
