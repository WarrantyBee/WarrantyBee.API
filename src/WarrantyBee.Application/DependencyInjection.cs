using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Options;
using WarrantyBee.Application.Abstractions.Persistence;
using WarrantyBee.Application.Abstractions.Services;
using WarrantyBee.Application.Configuration;
using WarrantyBee.Application.Services;
using WarrantyBee.Application.Decorators;

namespace WarrantyBee.Application;

public static class DependencyInjection
{
    public static IServiceCollection AddApplication(this IServiceCollection services)
    {
        services.AddLocalization();

        // Internal implementations
        services.AddScoped<AuthService>();
        services.AddScoped<UserService>();
        services.AddScoped<CountryService>();
        services.AddScoped<IOtpService, OtpService>();
        services.AddScoped<ILocalizationService, LocalizationService>();

        // Public interfaces decorated with Telemetry
        services.AddScoped<IAuthService>(sp => 
            new AuthServiceTelemetryDecorator(
                new AuthService(
                    sp.GetRequiredService<IOptions<AppConfiguration>>(),
                    sp.GetRequiredService<ITokenService>(),
                    sp.GetRequiredService<ICacheService>(),
                    sp.GetRequiredService<ICaptchaService>(),
                    sp.GetRequiredService<IOtpService>(),
                    sp.GetRequiredService<ITelemetryService>(),
                    sp.GetRequiredService<IUserRepository>(),
                    sp.GetRequiredService<IOtpRepository>(),
                    sp.GetRequiredService<IJobSchedulerClient>(),
                    sp.GetRequiredService<IEventPublisher>()), 
                sp.GetRequiredService<ITelemetryService>()));
        
        services.AddScoped<IUserService>(sp => 
            new UserServiceTelemetryDecorator(sp.GetRequiredService<UserService>(), sp.GetRequiredService<ITelemetryService>()));
            
        services.AddScoped<ICountryService>(sp => 
            new CountryServiceTelemetryDecorator(sp.GetRequiredService<CountryService>(), sp.GetRequiredService<ITelemetryService>()));

        return services;
    }
}
