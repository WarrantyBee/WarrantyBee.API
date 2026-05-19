using WarrantyBee.Application.Contracts.Identity;
using WarrantyBee.Application.Contracts.Users;
using WarrantyBee.Application.Contracts.Geographic;
using WarrantyBee.Domain.Enums;

namespace WarrantyBee.Application.Abstractions.Services;

public class NotificationPayload
{
    public string Recipient { get; set; } = string.Empty;
    public IDictionary<string, string> DynamicMacros { get; set; } = new Dictionary<string, string>();
    public NotificationType Type { get; set; }

    public NotificationPayload() { }
    public NotificationPayload(string recipient, IDictionary<string, string> dynamicMacros, NotificationType type)
    {
        Recipient = recipient;
        DynamicMacros = dynamicMacros;
        Type = type;
    }
}

public interface IAuthService
{
    Task<ILoginResponse> LoginAsync(LoginRequest request);
    Task<SignUpResponse> SignUpAsync(SignUpRequest request);
    Task ForgotPasswordAsync(ForgotPasswordRequest request);
    Task ResetPasswordAsync(ResetPasswordRequest request);
}

public interface IUserService
{
    Task<UserResponse?> GetAsync();
    Task<AvatarResponse> ChangeAvatarAsync(long userId, Stream avatarStream, string fileName, string contentType);
    Task UpdateProfileAsync(Persistence.ProfileUpdateRequest request);
}

public interface ICountryService
{
    Task<IEnumerable<Persistence.CountryDetailResponse>> GetAsync();
}

public interface IOtpService
{
    string Generate();
}

public interface ICacheService
{
    Task SetAsync(string key, string value, int? expirySeconds = null);
    Task<string?> GetAsync(string key);
    Task DeleteAsync(string key);
}

public interface ICaptchaService
{
    Task<bool> ValidateAsync(string captchaResponse);
}

public interface IEmailService
{
    Task SendAsync(NotificationPayload notification);
}

public interface IEmailTemplateService
{
    string Process(string templatePath, IDictionary<string, string> macros);
}

public interface IStorageService
{
    Task<string> UploadAsync(Stream fileStream, string fileName, string contentType);
    Task<bool> DeleteAsync(string id);
    Task<bool> DeleteByUrlAsync(string url);
}

public interface ITelemetryService
{
    void TrackEvent(string eventName, IDictionary<string, object>? properties = null);
    void Log(LogLevel level, string message, IDictionary<string, object>? context = null);
    void Log(LogLevel level, Exception exception, IDictionary<string, object>? context = null);
    void TrackMetric(string metricName, double value, IDictionary<string, object>? properties = null);
    void Flush();
}

public interface ITokenService
{
    string Generate(IDictionary<string, object> claims);
    IDictionary<string, object> Validate(string token);
}

public class AvatarResponse
{
    public string Url { get; set; } = string.Empty;

    public AvatarResponse() { }
    public AvatarResponse(string url) => Url = url;
}
