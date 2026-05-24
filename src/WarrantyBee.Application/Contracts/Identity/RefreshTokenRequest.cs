using WarrantyBee.Shared.Core.Contracts;

namespace WarrantyBee.Application.Contracts.Identity;

/// <summary>
/// Represents a request to refresh an access token using a refresh token.
/// </summary>
public class RefreshTokenRequest : BaseRequest
{
    /// <summary>
    /// Gets or sets the current (expired) access token.
    /// </summary>
    public string AccessToken { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the valid refresh token.
    /// </summary>
    public string RefreshToken { get; set; } = string.Empty;
}
