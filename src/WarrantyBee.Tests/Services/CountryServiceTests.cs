using FluentAssertions;
using Moq;
using WarrantyBee.Application.Abstractions.Persistence;
using WarrantyBee.Application.Contracts.Geographic;
using WarrantyBee.Application.Services;
using Xunit;

namespace WarrantyBee.Tests.Services;

public class CountryServiceTests
{
    private readonly Mock<ICountryRepository> _countryRepositoryMock;
    private readonly CountryService _service;

    public CountryServiceTests()
    {
        _countryRepositoryMock = new Mock<ICountryRepository>();
        _service = new CountryService(_countryRepositoryMock.Object);
    }

    [Fact]
    public async Task GetAsync_ReturnsCountries()
    {
        // Arrange
        var expectedCountries = new List<CountryDetailResponse>
        {
            new CountryDetailResponse(1, "Country", "CO", "COU", "1", "Official", "Cap", "1")
        };
        _countryRepositoryMock.Setup(r => r.GetAsync()).ReturnsAsync(expectedCountries);

        // Act
        var result = await _service.GetAsync();

        // Assert
        result.Should().BeEquivalentTo(expectedCountries);
    }
}
