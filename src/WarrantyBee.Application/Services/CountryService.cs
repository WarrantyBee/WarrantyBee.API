using WarrantyBee.Application.Abstractions.Persistence;
using WarrantyBee.Application.Abstractions.Services;

namespace WarrantyBee.Application.Services;

/// <summary>
/// Service for retrieving country-related information.
/// </summary>
public class CountryService : ICountryService
{
    private readonly ICountryRepository _repository;

    /// <summary>
    /// Initializes a new instance of the <see cref="CountryService"/> class.
    /// </summary>
    /// <param name="repository">The repository for country data.</param>
    public CountryService(ICountryRepository repository)
    {
        _repository = repository;
    }

    /// <summary>
    /// Retrieves a list of all countries.
    /// </summary>
    /// <returns>A collection of <see cref="CountryDetailResponse"/> objects.</returns>
    public async Task<IEnumerable<CountryDetailResponse>> GetAsync()
    {
        return await _repository.GetAsync();
    }
}
