using Microsoft.AspNetCore.Mvc;
using WarrantyBee.Shared.Infrastructure.Abstractions;
using WarrantyBee.Application.Abstractions.Services;

namespace WarrantyBee.Api.Controllers;

/// <summary>
/// Controller for managing country-related operations.
/// </summary>
[Route("api/[controller]")]
public class CountriesController : BaseController
{
    private readonly ICountryService _countryService;

    /// <summary>
    /// Initializes a new instance of the <see cref="CountriesController"/> class.
    /// </summary>
    /// <param name="countryService">The service used for country operations.</param>
    public CountriesController(ICountryService countryService)
    {
        _countryService = countryService;
    }

    /// <summary>
    /// Retrieves all countries.
    /// </summary>
    /// <returns>A list of countries within a standardized API response.</returns>
    [HttpGet]
    public async Task<IActionResult> Get()
    {
        var result = await _countryService.GetAsync();
        return OkResponse(result);
    }
}
