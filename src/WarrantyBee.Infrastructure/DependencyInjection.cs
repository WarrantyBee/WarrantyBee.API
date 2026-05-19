using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Configuration;
using WarrantyBee.Application.Abstractions.Persistence;
using WarrantyBee.Application.Abstractions.Services;
using WarrantyBee.Infrastructure.Persistence;
using WarrantyBee.Infrastructure.Services;

namespace WarrantyBee.Infrastructure;

public static class DependencyInjection
{
    public static IServiceCollection AddInfrastructure(this IServiceCollection services, IConfiguration configuration)
    {
        services.AddHttpContextAccessor();
        services.AddScoped<ICurrentUserContext, CurrentUserContext>();
        services.AddScoped<IDbConnectionFactory, DbConnectionFactory>();
        services.AddScoped<IUserRepository, UserRepository>();
        services.AddScoped<ICountryRepository, CountryRepository>();
        services.AddScoped<IOtpRepository, OtpRepository>();
        
        services.AddScoped<ITokenService, TokenService>();
        services.AddScoped<ICacheService, UpstashCacheService>();
        services.AddScoped<IStorageService, CloudinaryStorageService>();
        services.AddScoped<IEmailService, EmailService>();
        services.AddScoped<ITelemetryService, TelemetryService>();
        services.AddScoped<ICaptchaService, MockCaptchaService>();
        
        services.AddScoped<IEmailTemplateService>(sp => 
        {
            var templateRoot = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "Templates");
            // If not found in bin, try source
            if (!Directory.Exists(templateRoot))
            {
                templateRoot = Path.Combine(Directory.GetCurrentDirectory(), "src", "WarrantyBee.Infrastructure", "Templates");
            }
            return new EmailTemplateService(templateRoot);
        });

        return services;
    }
}
