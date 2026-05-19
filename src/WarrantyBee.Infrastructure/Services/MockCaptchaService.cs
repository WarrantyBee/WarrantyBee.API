using Microsoft.Extensions.Options;
using WarrantyBee.Application.Abstractions.Services;
using WarrantyBee.Application.Configuration;

namespace WarrantyBee.Infrastructure.Services;

/// <summary>
/// A mock implementation of the captcha verification service, used for development or testing environments.
/// </summary>
public class MockCaptchaService : ICaptchaService
{
    private readonly bool _isEnabled;

    /// <summary>
    /// Initializes a new instance of the <see cref="MockCaptchaService"/> class.
    /// </summary>
    /// <param name="config">The application configuration to check if captcha is enabled.</param>
    public MockCaptchaService(IOptions<AppConfiguration> config)
    {
        _isEnabled = config.Value.IsCaptchaEnabled;
    }

    /// <summary>
    /// Validates the captcha response token.
    /// </summary>
    /// <param name="captchaResponse">The captcha response token to validate.</param>
    /// <returns>A task returning <c>true</c> if validation is successful (or disabled); otherwise, <c>false</c>.</returns>
    public Task<bool> ValidateAsync(string captchaResponse)
    {
        if (!_isEnabled) return Task.FromResult(true);
        // For migration/testing, we'll allow any non-empty response
        return Task.FromResult(!string.IsNullOrWhiteSpace(captchaResponse));
    }
}
