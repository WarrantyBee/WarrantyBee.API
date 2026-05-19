using System.Text.Json;
using Dapper;
using WarrantyBee.Application.Abstractions.Persistence;
using WarrantyBee.Application.Contracts.Geographic;

namespace WarrantyBee.Infrastructure.Persistence;

/// <summary>
/// Repository for accessing country data from the database.
/// </summary>
public class CountryRepository : ICountryRepository
{
    private readonly IDbConnectionFactory _connectionFactory;

    /// <summary>
    /// Initializes a new instance of the <see cref="CountryRepository"/> class.
    /// </summary>
    /// <param name="connectionFactory">The factory used to create database connections.</param>
    public CountryRepository(IDbConnectionFactory connectionFactory)
    {
        _connectionFactory = connectionFactory;
    }

    /// <summary>
    /// Retrieves all countries from the database, including related regions, cultures, and timezones.
    /// </summary>
    /// <returns>A collection of <see cref="CountryDetailResponse"/> objects.</returns>
    public async Task<IEnumerable<CountryDetailResponse>> GetAsync()
    {
        using var connection = _connectionFactory.CreateConnection();
        var results = await connection.QueryAsync<dynamic>("CALL usp_GetCountries()");

        var countries = new List<CountryDetailResponse>();

        foreach (var row in results)
        {
            var country = new CountryDetailResponse(
                (long)row.id,
                (string)row.name,
                (string)row.iso2,
                (string)row.iso3,
                (string)row.code,
                (string)row.official_name,
                (string)row.capital,
                (string)row.phone_code)
            {
                Currency = row.currency != null ? JsonSerializer.Deserialize<CurrencyResponse>((string)row.currency, JsonOptions) : null,
                Regions = row.regions != null ? JsonSerializer.Deserialize<List<RegionResponse>>((string)row.regions, JsonOptions) : [],
                Cultures = row.cultures != null ? JsonSerializer.Deserialize<List<CultureResponse>>((string)row.cultures, JsonOptions) : [],
                Timezones = row.timezones != null ? JsonSerializer.Deserialize<List<TimeZoneResponse>>((string)row.timezones, JsonOptions) : []
            };
            countries.Add(country);
        }

        return countries;
    }

    private static readonly JsonSerializerOptions JsonOptions = new()
    {
        PropertyNamingPolicy = JsonNamingPolicy.CamelCase
    };
}
