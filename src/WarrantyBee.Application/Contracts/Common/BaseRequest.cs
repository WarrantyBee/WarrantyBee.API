namespace WarrantyBee.Application.Contracts.Common;

/// <summary>
/// Represents the base structure for all API requests.
/// </summary>
public abstract class BaseRequest
{
    /// <summary>
    /// Gets or sets the captcha response token for verification.
    /// </summary>
    public string? CaptchaResponse { get; set; }
}
