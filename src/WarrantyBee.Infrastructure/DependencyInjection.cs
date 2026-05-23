using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Hosting;
using WarrantyBee.Application.Abstractions.Persistence;
using WarrantyBee.Application.Abstractions.Services;
using WarrantyBee.Infrastructure.Persistence;
using WarrantyBee.Infrastructure.Services;
using WarrantyBee.Infrastructure.Background;

namespace WarrantyBee.Infrastructure;

public static class DependencyInjection
{
    public static IServiceCollection AddInfrastructure(this IServiceCollection services, IConfiguration configuration)
    {
        services.AddHttpContextAccessor();
        services.AddHttpClient();

        services.AddScoped<ICurrentUserContext, CurrentUserContext>();
        services.AddScoped<IDbConnectionFactory, DbConnectionFactory>();
        services.AddScoped<IUserRepository, UserRepository>();
        services.AddScoped<ICountryRepository, CountryRepository>();
        services.AddScoped<IOtpRepository, OtpRepository>();
        
        // API Key Management Repositories
        services.AddScoped<IApiClientRepository, ApiClientRepository>();
        services.AddScoped<IApiKeyRepository, ApiKeyRepository>();
        
        services.AddScoped<ITokenService, TokenService>();
        services.AddScoped<ICacheService, UpstashCacheService>();
        services.AddScoped<IStorageService, CloudinaryStorageService>();
        services.AddScoped<IEmailService, EmailService>();
        services.AddScoped<ITelemetryService, TelemetryService>();
        services.AddScoped<ICaptchaService, ReCaptchaService>();
        services.AddScoped<IEventPublisher, EventPublisher>();
        services.AddScoped<IJobSchedulerClient, JobSchedulerClient>();

        // High-scale Background processing
        services.AddSingleton<IBackgroundTaskQueue, BackgroundTaskQueue>();
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
