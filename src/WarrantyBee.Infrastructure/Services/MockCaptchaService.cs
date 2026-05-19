using Microsoft.Extensions.Options;
using WarrantyBee.Application.Abstractions.Services;
using WarrantyBee.Application.Configuration;

namespace WarrantyBee.Infrastructure.Services;

public class MockCaptchaService : ICaptchaService
{
    private readonly bool _isEnabled;

    public MockCaptchaService(IOptions<AppConfiguration> config)
    {
        _isEnabled = config.Value.IsCaptchaEnabled;
    }

    public Task<bool> ValidateAsync(string captchaResponse)
    {
        if (!_isEnabled) return Task.FromResult(true);
        // For migration/testing, we'll allow any non-empty response
        return Task.FromResult(!string.IsNullOrWhiteSpace(captchaResponse));
    }
}
