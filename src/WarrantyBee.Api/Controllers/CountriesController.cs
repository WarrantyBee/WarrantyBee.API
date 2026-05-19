using Microsoft.AspNetCore.Mvc;
using WarrantyBee.Application.Abstractions.Services;

namespace WarrantyBee.Api.Controllers;

[Route("api/[controller]")]
public class CountriesController : BaseController
{
    private readonly ICountryService _countryService;

    public CountriesController(ICountryService countryService)
    {
        _countryService = countryService;
    }

    [HttpGet]
    public async Task<IActionResult> Get()
    {
        var result = await _countryService.GetAsync();
        return OkResponse(result);
    }
}
