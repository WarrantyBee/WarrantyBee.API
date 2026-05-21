using Microsoft.Extensions.DependencyInjection;
using WarrantyBee.Application.Abstractions.Services;
using WarrantyBee.Application.Services;
using WarrantyBee.Application.Decorators;

namespace WarrantyBee.Application;

public static class DependencyInjection
{
    public static IServiceCollection AddApplication(this IServiceCollection services)
    {
        // Internal implementations
        services.AddScoped<AuthService>();
        services.AddScoped<UserService>();
        services.AddScoped<CountryService>();
        services.AddScoped<IOtpService, OtpService>();

        // Public interfaces decorated with Telemetry
        services.AddScoped<IAuthService>(sp => 
            new AuthServiceTelemetryDecorator(sp.GetRequiredService<AuthService>(), sp.GetRequiredService<ITelemetryService>()));
        
        services.AddScoped<IUserService>(sp => 
            new UserServiceTelemetryDecorator(sp.GetRequiredService<UserService>(), sp.GetRequiredService<ITelemetryService>()));
            
        services.AddScoped<ICountryService>(sp => 
            new CountryServiceTelemetryDecorator(sp.GetRequiredService<CountryService>(), sp.GetRequiredService<ITelemetryService>()));

        return services;
    }
}
