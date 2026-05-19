using WarrantyBee.Application.Contracts.Geographic;

namespace WarrantyBee.Application.Abstractions.Persistence;

public class CountryDetailResponse : CountryResponse
{
    public CountryDetailResponse() { }
    public CountryDetailResponse(
        long id, string name, string iso2, string iso3, string code, 
        string officialName, string capital, string phoneCode) 
        : base(id, name, iso2, iso3, code, officialName, capital, phoneCode) {}

    public CurrencyResponse? Currency { get; set; }
    public IEnumerable<RegionResponse>? Regions { get; set; }
    public IEnumerable<CultureResponse>? Cultures { get; set; }
    public IEnumerable<TimeZoneResponse>? Timezones { get; set; }
}

public interface ICountryRepository
{
    Task<IEnumerable<CountryDetailResponse>> GetAsync();
}
