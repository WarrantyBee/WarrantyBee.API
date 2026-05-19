namespace WarrantyBee.Application.Contracts.Geographic;

public class CountryResponse
{
    public long Id { get; set; }
    public string Name { get; set; } = string.Empty;
    public string Iso2 { get; set; } = string.Empty;
    public string Iso3 { get; set; } = string.Empty;
    public string Code { get; set; } = string.Empty;
    public string OfficialName { get; set; } = string.Empty;
    public string Capital { get; set; } = string.Empty;
    public string PhoneCode { get; set; } = string.Empty;

    public CountryResponse() { }
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

public class RegionResponse
{
    public long Id { get; set; }
    public string Name { get; set; } = string.Empty;
    public string Iso { get; set; } = string.Empty;
    public string? Code { get; set; }
    public long TimezoneId { get; set; }

    public RegionResponse() { }
    public RegionResponse(long id, string name, string iso, string? code, long timezoneId)
    {
        Id = id;
        Name = name;
        Iso = iso;
        Code = code;
        TimezoneId = timezoneId;
    }
}

public class TimeZoneResponse
{
    public long Id { get; set; }
    public string Name { get; set; } = string.Empty;
    public string Abbreviation { get; set; } = string.Empty;
    public short OffsetMinutes { get; set; }
    public bool Dst { get; set; }
    public short CurrentOffsetMinutes { get; set; }

    public TimeZoneResponse() { }
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

public class LanguageResponse
{
    public long Id { get; set; }
    public string Name { get; set; } = string.Empty;
    public string Iso { get; set; } = string.Empty;
    public string NativeName { get; set; } = string.Empty;

    public LanguageResponse() { }
    public LanguageResponse(long id, string name, string iso, string nativeName)
    {
        Id = id;
        Name = name;
        Iso = iso;
        NativeName = nativeName;
    }
}

public class CultureResponse
{
    public long Id { get; set; }
    public string Iso { get; set; } = string.Empty;
    public bool Rtl { get; set; }
    public LanguageResponse? Language { get; set; }

    public CultureResponse() { }
    public CultureResponse(long id, string iso, bool rtl, LanguageResponse language)
    {
        Id = id;
        Iso = iso;
        Rtl = rtl;
        Language = language;
    }
}

public class CurrencyResponse
{
    public long Id { get; set; }
    public string Name { get; set; } = string.Empty;
    public string Iso { get; set; } = string.Empty;
    public string Code { get; set; } = string.Empty;
    public string Symbol { get; set; } = string.Empty;
    public byte MinorUnit { get; set; }

    public CurrencyResponse() { }
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
