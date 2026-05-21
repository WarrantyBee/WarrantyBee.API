namespace WarrantyBee.Application.Abstractions.Services;

/// <summary>
/// Service for providing localized strings.
/// </summary>
public interface ILocalizationService
{
    /// <summary>
    /// Gets a localized string for the specified key.
    /// </summary>
    /// <param name="key">The resource key.</param>
    /// <returns>The localized string.</returns>
    string GetString(string key);

    /// <summary>
    /// Gets a localized string for the specified key and culture.
    /// </summary>
    /// <param name="key">The resource key.</param>
    /// <param name="culture">The ISO culture code (e.g., 'en-US').</param>
    /// <returns>The localized string.</returns>
    string GetString(string key, string culture);
}
