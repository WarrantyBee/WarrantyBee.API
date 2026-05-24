using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Configuration;
using WarrantyBee.Application.Abstractions.Persistence;
using WarrantyBee.Shared.Infrastructure.Abstractions;
using WarrantyBee.API.Infrastructure.Persistence;
using WarrantyBee.API.Infrastructure.Background;
using WarrantyBee.API.Infrastructure.Services;
using WarrantyBee.Shared.Infrastructure;
using WarrantyBee.Shared.Infrastructure.Services;
using WarrantyBee.Application.Abstractions.Services;

namespace WarrantyBee.API.Infrastructure;

public static class DependencyInjection
{
    public static IServiceCollection AddInfrastructure(this IServiceCollection services, IConfiguration configuration)
    {
        services.AddHttpContextAccessor();
        services.AddHttpClient();

        // Register Shared Infrastructure (Telemetry, Cache, Background Queue, Filters, EventPublisher, DbConnectionFactory, CurrentUserContext)
        services.AddWarrantyBeeInfrastructure();

        services.AddScoped<IUserRepository, UserRepository>();
        services.AddScoped<IRefreshTokenRepository, RefreshTokenRepository>();
        services.AddScoped<IBusinessRepository, BusinessRepository>();
        services.AddScoped<IOnboardingRepository, OnboardingRepository>();
        services.AddScoped<IProductRepository, ProductRepository>();
        services.AddScoped<IVaultRepository, VaultRepository>();
        services.AddScoped<IClaimRepository, ClaimRepository>();
        services.AddScoped<ICountryRepository, CountryRepository>();
        services.AddScoped<IOtpRepository, OtpRepository>();

        // API Key Management Repositories
        services.AddScoped<IApiClientRepository, ApiClientRepository>();
        services.AddScoped<IApiKeyRepository, ApiKeyRepository>();

        services.AddScoped<ITokenService, TokenService>();
        services.AddScoped<IStorageService, CloudinaryStorageService>();
        services.AddScoped<IEmailService, EmailService>();
        services.AddScoped<ICaptchaService, ReCaptchaService>();
        services.AddScoped<IJobSchedulerClient, JobSchedulerClient>();

        // High-scale Background processing
        services.AddHostedService<QueuedHostedService>();

        services.AddScoped<IEmailTemplateService>(sp => 
        {
            var templateRoot = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "Templates");
            if (!Directory.Exists(templateRoot))
            {
                templateRoot = Path.Combine(Directory.GetCurrentDirectory(), "src", "WarrantyBee.Infrastructure", "Templates");
            }
            return new EmailTemplateService(templateRoot);
        });

        return services;
    }
}
