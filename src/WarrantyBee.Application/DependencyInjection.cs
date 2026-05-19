using Microsoft.Extensions.DependencyInjection;
using WarrantyBee.Application.Abstractions.Services;
using WarrantyBee.Application.Services;

namespace WarrantyBee.Application;

public static class DependencyInjection
{
    public static IServiceCollection AddApplication(this IServiceCollection services)
    {
        services.AddScoped<IAuthService, AuthService>();
        services.AddScoped<IUserService, UserService>();
        services.AddScoped<ICountryService, CountryService>();
        services.AddScoped<IOtpService, OtpService>();
        return services;
    }
}
