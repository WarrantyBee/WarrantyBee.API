namespace WarrantyBee.Application.Contracts.Geographic;

/// <summary>
/// Represents a country in the system.
/// </summary>
public class CountryResponse
{
    /// <summary>
    /// Gets or sets the country identifier.
    /// </summary>
    public long Id { get; set; }

    /// <summary>
    /// Gets or sets the common name of the country.
    /// </summary>
    public string Name { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the ISO 3166-1 alpha-2 country code.
    /// </summary>
    public string Iso2 { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the ISO 3166-1 alpha-3 country code.
    /// </summary>
    public string Iso3 { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the numeric country code.
    /// </summary>
    public string Code { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the official name of the country.
    /// </summary>
    public string OfficialName { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the capital city of the country.
    /// </summary>
    public string Capital { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the international phone dialing code.
    /// </summary>
    public string PhoneCode { get; set; } = string.Empty;

    /// <summary>
    /// Initializes a new instance of the <see cref="CountryResponse"/> class.
    /// </summary>
    public CountryResponse() { }

    /// <summary>
    /// Initializes a new instance of the <see cref="CountryResponse"/> class with detailed information.
    /// </summary>
    /// <param name="id">The country identifier.</param>
    /// <param name="name">The common name.</param>
    /// <param name="iso2">The alpha-2 code.</param>
    /// <param name="iso3">The alpha-3 code.</param>
    /// <param name="code">The numeric code.</param>
    /// <param name="officialName">The official name.</param>
    /// <param name="capital">The capital city.</param>
    /// <param name="phoneCode">The phone code.</param>
    public CountryResponse(long id, string name, string iso2, string iso3, string code, string officialName, string capital, string phoneCode)
    {
        Id = id;
        Name = name;
        Iso2 = iso2;
        Iso3 = iso3;
        Code = code;
        OfficialName = officialName;
        Capital = capital;
        PhoneCode = phoneCode;
    }
}

/// <summary>
/// Represents a geographic region or state within a country.
/// </summary>
public class RegionResponse
{
    /// <summary>
    /// Gets or sets the region identifier.
    /// </summary>
    public long Id { get; set; }

    /// <summary>
    /// Gets or sets the name of the region.
    /// </summary>
    public string Name { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the ISO code of the region.
    /// </summary>
    public string Iso { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the optional code for the region.
    /// </summary>
    public string? Code { get; set; }

    /// <summary>
    /// Gets or sets the default timezone identifier for this region.
    /// </summary>
    public long TimezoneId { get; set; }

    /// <summary>
    /// Initializes a new instance of the <see cref="RegionResponse"/> class.
    /// </summary>
    public RegionResponse() { }

    /// <summary>
    /// Initializes a new instance of the <see cref="RegionResponse"/> class with detailed information.
    /// </summary>
    /// <param name="id">The region identifier.</param>
    /// <param name="name">The region name.</param>
    /// <param name="iso">The ISO code.</param>
    /// <param name="code">The region code.</param>
    /// <param name="timezoneId">The default timezone identifier.</param>
    public RegionResponse(long id, string name, string iso, string? code, long timezoneId)
    {
        Id = id;
        Name = name;
        Iso = iso;
        Code = code;
        TimezoneId = timezoneId;
    }
}

/// <summary>
/// Represents a timezone.
/// </summary>
public class TimeZoneResponse
{
    /// <summary>
    /// Gets or sets the timezone identifier.
    /// </summary>
    public long Id { get; set; }

    /// <summary>
    /// Gets or sets the name of the timezone (e.g., "Pacific Standard Time").
    /// </summary>
    public string Name { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the abbreviation of the timezone (e.g., "PST").
    /// </summary>
    public string Abbreviation { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the standard offset from UTC in minutes.
    /// </summary>
    public short OffsetMinutes { get; set; }

    /// <summary>
    /// Gets or sets a value indicating whether Daylight Saving Time (DST) is currently active.
    /// </summary>
    public bool Dst { get; set; }

    /// <summary>
    /// Gets or sets the current offset from UTC in minutes, accounting for DST.
    /// </summary>
    public short CurrentOffsetMinutes { get; set; }

    /// <summary>
    /// Initializes a new instance of the <see cref="TimeZoneResponse"/> class.
    /// </summary>
    public TimeZoneResponse() { }

    /// <summary>
    /// Initializes a new instance of the <see cref="TimeZoneResponse"/> class with detailed information.
    /// </summary>
    /// <param name="id">The timezone identifier.</param>
    /// <param name="name">The timezone name.</param>
    /// <param name="abbreviation">The abbreviation.</param>
    /// <param name="offsetMinutes">The standard offset.</param>
    /// <param name="dst">Whether DST is active.</param>
    /// <param name="currentOffsetMinutes">The current offset.</param>
    public TimeZoneResponse(long id, string name, string abbreviation, short offsetMinutes, bool dst, short currentOffsetMinutes)
    {
        Id = id;
        Name = name;
        Abbreviation = abbreviation;
        OffsetMinutes = offsetMinutes;
        Dst = dst;
        CurrentOffsetMinutes = currentOffsetMinutes;
    }
}

/// <summary>
/// Represents a language.
/// </summary>
public class LanguageResponse
{
    /// <summary>
    /// Gets or sets the language identifier.
    /// </summary>
    public long Id { get; set; }

    /// <summary>
    /// Gets or sets the name of the language.
    /// </summary>
    public string Name { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the ISO 639-1 language code.
    /// </summary>
    public string Iso { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the native name of the language.
    /// </summary>
    public string NativeName { get; set; } = string.Empty;

    /// <summary>
    /// Initializes a new instance of the <see cref="LanguageResponse"/> class.
    /// </summary>
    public LanguageResponse() { }

    /// <summary>
    /// Initializes a new instance of the <see cref="LanguageResponse"/> class with detailed information.
    /// </summary>
    /// <param name="id">The language identifier.</param>
    /// <param name="name">The language name.</param>
    /// <param name="iso">The ISO code.</param>
    /// <param name="nativeName">The native name.</param>
    public LanguageResponse(long id, string name, string iso, string nativeName)
    {
        Id = id;
        Name = name;
        Iso = iso;
        NativeName = nativeName;
    }
}

/// <summary>
/// Represents a culture, combining language and regional settings.
/// </summary>
public class CultureResponse
{
    /// <summary>
    /// Gets or sets the culture identifier.
    /// </summary>
    public long Id { get; set; }

    /// <summary>
    /// Gets or sets the culture ISO code (e.g., "en-US").
    /// </summary>
    public string Iso { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets a value indicating whether the culture uses right-to-left (RTL) text orientation.
    /// </summary>
    public bool Rtl { get; set; }

    /// <summary>
    /// Gets or sets the associated language details.
    /// </summary>
    public LanguageResponse? Language { get; set; }

    /// <summary>
    /// Initializes a new instance of the <see cref="CultureResponse"/> class.
    /// </summary>
    public CultureResponse() { }

    /// <summary>
    /// Initializes a new instance of the <see cref="CultureResponse"/> class with detailed information.
    /// </summary>
    /// <param name="id">The culture identifier.</param>
    /// <param name="iso">The ISO code.</param>
    /// <param name="rtl">Whether it is RTL.</param>
    /// <param name="language">The language details.</param>
    public CultureResponse(long id, string iso, bool rtl, LanguageResponse language)
    {
        Id = id;
        Iso = iso;
        Rtl = rtl;
        Language = language;
    }
}

/// <summary>
/// Represents a currency.
/// </summary>
public class CurrencyResponse
{
    /// <summary>
    /// Gets or sets the currency identifier.
    /// </summary>
    public long Id { get; set; }

    /// <summary>
    /// Gets or sets the name of the currency.
    /// </summary>
    public string Name { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the ISO 4217 currency code (e.g., "USD").
    /// </summary>
    public string Iso { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the numeric currency code.
    /// </summary>
    public string Code { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the currency symbol (e.g., "$").
    /// </summary>
    public string Symbol { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the number of decimal places for the currency's minor unit.
    /// </summary>
    public byte MinorUnit { get; set; }

    /// <summary>
    /// Initializes a new instance of the <see cref="CurrencyResponse"/> class.
    /// </summary>
    public CurrencyResponse() { }

    /// <summary>
    /// Initializes a new instance of the <see cref="CurrencyResponse"/> class with detailed information.
    /// </summary>
    /// <param name="id">The currency identifier.</param>
    /// <param name="name">The currency name.</param>
    /// <param name="iso">The ISO code.</param>
    /// <param name="code">The numeric code.</param>
    /// <param name="symbol">The symbol.</param>
    /// <param name="minorUnit">The minor unit decimal places.</param>
    public CurrencyResponse(long id, string name, string iso, string code, string symbol, byte minorUnit)
    {
        Id = id;
        Name = name;
        Iso = iso;
        Code = code;
        Symbol = symbol;
        MinorUnit = minorUnit;
    }
}
