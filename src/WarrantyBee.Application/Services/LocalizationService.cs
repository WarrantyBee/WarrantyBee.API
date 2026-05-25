using System.Globalization;
using Microsoft.Extensions.Localization;
using WarrantyBee.Shared.Infrastructure.Abstractions;
using WarrantyBee.Application.Abstractions.Services;
using WarrantyBee.Shared.Core.Contracts;

namespace WarrantyBee.Application.Services;

/// <summary>
/// Implementation of <see cref="ILocalizationService"/> using <see cref="IStringLocalizer"/>.
/// </summary>
public class LocalizationService : ILocalizationService
{
    private readonly IStringLocalizer _localizer;

    public LocalizationService(IStringLocalizerFactory factory)
    {
        // We use a dummy type to anchor the resource location
        _localizer = factory.Create("Errors", "WarrantyBee.API.Application");
    }

    public string GetString(string key)
    {
        return _localizer[key];
    }

    public string GetString(string key, string culture)
    {
        var currentCulture = CultureInfo.CurrentUICulture;
        try
        {
            CultureInfo.CurrentUICulture = new CultureInfo(culture);
            return _localizer[key];
        }
        catch
        {
            return _localizer[key];
        }
        finally
        {
            CultureInfo.CurrentUICulture = currentCulture;
        }
    }
}