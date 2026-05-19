using WarrantyBee.Application.Abstractions.Persistence;
using WarrantyBee.Application.Abstractions.Services;

namespace WarrantyBee.Application.Services;

public class CountryService : ICountryService
{
    private readonly ICountryRepository _repository;

    public CountryService(ICountryRepository repository)
    {
        _repository = repository;
    }

    public async Task<IEnumerable<CountryDetailResponse>> GetAsync()
    {
        return await _repository.GetAsync();
    }
}
