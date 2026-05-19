using WarrantyBee.Application.Contracts.Identity;
using WarrantyBee.Application.Contracts.Users;
using WarrantyBee.Application.Contracts.Geographic;
using WarrantyBee.Domain.Enums;

namespace WarrantyBee.Application.Abstractions.Services;

/// <summary>
/// Represents the payload for a notification to be sent to a user.
/// </summary>
public class NotificationPayload
{
    /// <summary>
    /// Gets or sets the recipient of the notification (e.g., email address).
    /// </summary>
    public string Recipient { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the dynamic macros to be replaced in the notification template.
    /// </summary>
    public IDictionary<string, string> DynamicMacros { get; set; } = new Dictionary<string, string>();

    /// <summary>
    /// Gets or sets the type of notification.
    /// </summary>
    public NotificationType Type { get; set; }

    /// <summary>
    /// Initializes a new instance of the <see cref="NotificationPayload"/> class.
    /// </summary>
    public NotificationPayload() { }

    /// <summary>
    /// Initializes a new instance of the <see cref="NotificationPayload"/> class with specified details.
    /// </summary>
    /// <param name="recipient">The recipient.</param>
    /// <param name="dynamicMacros">The dynamic macros.</param>
    /// <param name="type">The notification type.</param>
    public NotificationPayload(string recipient, IDictionary<string, string> dynamicMacros, NotificationType type)
    {
        Recipient = recipient;
        DynamicMacros = dynamicMacros;
        Type = type;
    }
}

/// <summary>
/// Defines the contract for authentication-related services.
/// </summary>
public interface IAuthService
{
    /// <summary>
    /// Authenticates a user based on the login request.
    /// </summary>
    /// <param name="request">The login request.</param>
    /// <returns>A login response containing the authentication result.</returns>
    Task<ILoginResponse> LoginAsync(LoginRequest request);

    /// <summary>
    /// Registers a new user based on the sign-up request.
    /// </summary>
    /// <param name="request">The sign-up request.</param>
    /// <returns>A sign-up response containing the registration result.</returns>
    Task<SignUpResponse> SignUpAsync(SignUpRequest request);

    /// <summary>
    /// Initiates the forgot password process for a user.
    /// </summary>
    /// <param name="request">The forgot password request.</param>
    /// <returns>A task representing the asynchronous operation.</returns>
    Task ForgotPasswordAsync(ForgotPasswordRequest request);

    /// <summary>
    /// Resets a user's password based on the reset password request.
    /// </summary>
    /// <param name="request">The reset password request.</param>
    /// <returns>A task representing the asynchronous operation.</returns>
    Task ResetPasswordAsync(ResetPasswordRequest request);
}

/// <summary>
/// Defines the contract for user-related services.
/// </summary>
public interface IUserService
{
    /// <summary>
    /// Retrieves the profile information of the current user.
    /// </summary>
    /// <returns>A user response if found; otherwise, null.</returns>
    Task<UserResponse?> GetAsync();

    /// <summary>
    /// Changes the avatar of a user.
    /// </summary>
    /// <param name="userId">The user identifier.</param>
    /// <param name="avatarStream">The stream of the avatar image file.</param>
    /// <param name="fileName">The name of the file.</param>
    /// <param name="contentType">The content type of the file.</param>
    /// <returns>An avatar response containing the new avatar URL.</returns>
    Task<AvatarResponse> ChangeAvatarAsync(long userId, Stream avatarStream, string fileName, string contentType);

    /// <summary>
    /// Updates the profile information of the current user.
    /// </summary>
    /// <param name="request">The profile update request.</param>
    /// <returns>A task representing the asynchronous operation.</returns>
    Task UpdateProfileAsync(Persistence.ProfileUpdateRequest request);
}

/// <summary>
/// Defines the contract for country-related services.
/// </summary>
public interface ICountryService
{
    /// <summary>
    /// Retrieves all countries with their detailed information.
    /// </summary>
    /// <returns>A collection of country details.</returns>
    Task<IEnumerable<Persistence.CountryDetailResponse>> GetAsync();
}

/// <summary>
/// Defines the contract for One-Time Password (OTP) generation.
/// </summary>
public interface IOtpService
{
    /// <summary>
    /// Generates a new OTP.
    /// </summary>
    /// <returns>A string representation of the generated OTP.</returns>
    string Generate();
}

/// <summary>
/// Defines the contract for cache-related services.
/// </summary>
public interface ICacheService
{
    /// <summary>
    /// Sets a value in the cache with an optional expiry.
    /// </summary>
    /// <param name="key">The cache key.</param>
    /// <param name="value">The value to cache.</param>
    /// <param name="expirySeconds">The optional expiry time in seconds.</param>
    /// <returns>A task representing the asynchronous operation.</returns>
    Task SetAsync(string key, string value, int? expirySeconds = null);

    /// <summary>
    /// Retrieves a value from the cache.
    /// </summary>
    /// <param name="key">The cache key.</param>
    /// <returns>The cached value if found; otherwise, null.</returns>
    Task<string?> GetAsync(string key);

    /// <summary>
    /// Deletes a value from the cache.
    /// </summary>
    /// <param name="key">The cache key.</param>
    /// <returns>A task representing the asynchronous operation.</returns>
    Task DeleteAsync(string key);
}

/// <summary>
/// Defines the contract for CAPTCHA validation services.
/// </summary>
public interface ICaptchaService
{
    /// <summary>
    /// Validates a CAPTCHA response.
    /// </summary>
    /// <param name="captchaResponse">The CAPTCHA response to validate.</param>
    /// <returns>True if the CAPTCHA is valid; otherwise, false.</returns>
    Task<bool> ValidateAsync(string captchaResponse);
}

/// <summary>
/// Defines the contract for email sending services.
/// </summary>
public interface IEmailService
{
    /// <summary>
    /// Sends an email based on the specified notification payload.
    /// </summary>
    /// <param name="notification">The notification payload.</param>
    /// <returns>A task representing the asynchronous operation.</returns>
    Task SendAsync(NotificationPayload notification);
}

/// <summary>
/// Defines the contract for email template processing services.
/// </summary>
public interface IEmailTemplateService
{
    /// <summary>
    /// Processes an email template by replacing macros with their corresponding values.
    /// </summary>
    /// <param name="templatePath">The path to the email template file.</param>
    /// <param name="macros">A dictionary of macros and their replacement values.</param>
    /// <returns>The processed email template content.</returns>
    string Process(string templatePath, IDictionary<string, string> macros);
}

/// <summary>
/// Defines the contract for file storage services.
/// </summary>
public interface IStorageService
{
    /// <summary>
    /// Uploads a file to the storage service.
    /// </summary>
    /// <param name="fileStream">The stream of the file to upload.</param>
    /// <param name="fileName">The name of the file.</param>
    /// <param name="contentType">The content type of the file.</param>
    /// <returns>The URL of the uploaded file.</returns>
    Task<string> UploadAsync(Stream fileStream, string fileName, string contentType);

    /// <summary>
    /// Deletes a file from the storage service by its unique identifier.
    /// </summary>
    /// <param name="id">The unique identifier of the file.</param>
    /// <returns>True if the file was deleted successfully; otherwise, false.</returns>
    Task<bool> DeleteAsync(string id);

    /// <summary>
    /// Deletes a file from the storage service by its URL.
    /// </summary>
    /// <param name="url">The URL of the file to delete.</param>
    /// <returns>True if the file was deleted successfully; otherwise, false.</returns>
    Task<bool> DeleteByUrlAsync(string url);
}

/// <summary>
/// Defines the contract for telemetry and logging services.
/// </summary>
public interface ITelemetryService
{
    /// <summary>
    /// Tracks an application event.
    /// </summary>
    /// <param name="eventName">The name of the event.</param>
    /// <param name="properties">Optional properties associated with the event.</param>
    void TrackEvent(string eventName, IDictionary<string, object>? properties = null);

    /// <summary>
    /// Logs a message with a specified severity level.
    /// </summary>
    /// <param name="level">The severity level.</param>
    /// <param name="message">The message to log.</param>
    /// <param name="context">Optional context information.</param>
    void Log(LogLevel level, string message, IDictionary<string, object>? context = null);

    /// <summary>
    /// Logs an exception with a specified severity level.
    /// </summary>
    /// <param name="level">The severity level.</param>
    /// <param name="exception">The exception to log.</param>
    /// <param name="context">Optional context information.</param>
    void Log(LogLevel level, Exception exception, IDictionary<string, object>? context = null);

    /// <summary>
    /// Tracks a numeric metric.
    /// </summary>
    /// <param name="metricName">The name of the metric.</param>
    /// <param name="value">The value of the metric.</param>
    /// <param name="properties">Optional properties associated with the metric.</param>
    void TrackMetric(string metricName, double value, IDictionary<string, object>? properties = null);

    /// <summary>
    /// Flushes all tracked telemetry data.
    /// </summary>
    void Flush();
}

/// <summary>
/// Defines the contract for token-related services.
/// </summary>
public interface ITokenService
{
    /// <summary>
    /// Generates a token based on the specified claims.
    /// </summary>
    /// <param name="claims">A dictionary of claims to include in the token.</param>
    /// <returns>A string representation of the generated token.</returns>
    string Generate(IDictionary<string, object> claims);

    /// <summary>
    /// Validates a token and returns the claims it contains.
    /// </summary>
    /// <param name="token">The token to validate.</param>
    /// <returns>A dictionary of claims extracted from the token.</returns>
    IDictionary<string, object> Validate(string token);
}

/// <summary>
/// Represents a response containing an avatar URL.
/// </summary>
public class AvatarResponse
{
    /// <summary>
    /// Gets or sets the URL of the avatar image.
    /// </summary>
    public string Url { get; set; } = string.Empty;

    /// <summary>
    /// Initializes a new instance of the <see cref="AvatarResponse"/> class.
    /// </summary>
    public AvatarResponse() { }

    /// <summary>
    /// Initializes a new instance of the <see cref="AvatarResponse"/> class with a specified URL.
    /// </summary>
    /// <param name="url">The avatar URL.</param>
    public AvatarResponse(string url) => Url = url;
}
