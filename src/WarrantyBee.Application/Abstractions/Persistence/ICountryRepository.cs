using WarrantyBee.Application.Contracts.Geographic;

namespace WarrantyBee.Application.Abstractions.Persistence;

/// <summary>
/// Represents detailed information about a country, extending the basic country response.
/// </summary>
public class CountryDetailResponse : CountryResponse
{
    /// <summary>
    /// Initializes a new instance of the <see cref="CountryDetailResponse"/> class.
    /// </summary>
    public CountryDetailResponse() { }

    /// <summary>
    /// Initializes a new instance of the <see cref="CountryDetailResponse"/> class with specified details.
    /// </summary>
    /// <param name="id">The unique identifier of the country.</param>
    /// <param name="name">The name of the country.</param>
    /// <param name="iso2">The ISO 3166-1 alpha-2 code of the country.</param>
    /// <param name="iso3">The ISO 3166-1 alpha-3 code of the country.</param>
    /// <param name="code">The numeric code of the country.</param>
    /// <param name="officialName">The official name of the country.</param>
    /// <param name="capital">The capital city of the country.</param>
    /// <param name="phoneCode">The international phone dialing code.</param>
    public CountryDetailResponse(
        long id, string name, string iso2, string iso3, string code, 
        string officialName, string capital, string phoneCode) 
        : base(id, name, iso2, iso3, code, officialName, capital, phoneCode) {}

    /// <summary>
    /// Gets or sets the currency used in the country.
    /// </summary>
    public CurrencyResponse? Currency { get; set; }

    /// <summary>
    /// Gets or sets the regions within the country.
    /// </summary>
    public IEnumerable<RegionResponse>? Regions { get; set; }

    /// <summary>
    /// Gets or sets the cultures associated with the country.
    /// </summary>
    public IEnumerable<CultureResponse>? Cultures { get; set; }

    /// <summary>
    /// Gets or sets the time zones applicable to the country.
    /// </summary>
    public IEnumerable<TimeZoneResponse>? Timezones { get; set; }
}

/// <summary>
/// Defines the contract for country-related persistence operations.
/// </summary>
public interface ICountryRepository
{
    /// <summary>
    /// Retrieves all countries with their detailed information.
    /// </summary>
    /// <returns>A collection of <see cref="CountryDetailResponse"/>.</returns>
    Task<IEnumerable<CountryDetailResponse>> GetAsync();
}
