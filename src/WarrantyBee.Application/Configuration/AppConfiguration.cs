namespace WarrantyBee.Application.Configuration;

/// <summary>
/// Represents the root configuration object for the application.
/// </summary>
public class AppConfiguration
{
    /// <summary>
    /// Gets or sets the application name.
    /// </summary>
    public string? Name { get; set; }

    /// <summary>
    /// Gets or sets the application environment.
    /// </summary>
    public string? Environment { get; set; }

    /// <summary>
    /// Gets or sets a value indicating whether CAPTCHA is enabled.
    /// </summary>
    public bool IsCaptchaEnabled { get; set; }

    /// <summary>
    /// Gets or sets the data source configuration.
    /// </summary>
    public DataSourceConfiguration? DataSource { get; set; }

    /// <summary>
    /// Gets or sets the Upstash (Redis) configuration.
    /// </summary>
    public UpstashConfiguration? Upstash { get; set; }

    /// <summary>
    /// Gets or sets the Better Stack (logging/telemetry) configuration.
    /// </summary>
    public BetterStackConfiguration? BetterStack { get; set; }

    /// <summary>
    /// Gets or sets the JWT token configuration.
    /// </summary>
    public JwtTokenConfiguration? Jwt { get; set; }

    /// <summary>
    /// Gets or sets the ReCaptcha configuration.
    /// </summary>
    public ReCaptchaConfiguration? ReCaptcha { get; set; }

    /// <summary>
    /// Gets or sets the SMTP configuration for sending emails.
    /// </summary>
    public SmtpConfiguration? Smtp { get; set; }

    /// <summary>
    /// Gets or sets the OTP configuration.
    /// </summary>
    public OtpConfiguration? Otp { get; set; }

    /// <summary>
    /// Gets or sets the user profile configuration.
    /// </summary>
    public ProfileConfiguration? Profile { get; set; }

    /// <summary>
    /// Gets or sets the Cloudinary configuration for image storage.
    /// </summary>
    public CloudinaryConfiguration? Cloudinary { get; set; }

    /// <summary>
    /// Gets or sets the Facebook OAuth configuration.
    /// </summary>
    public FacebookConfiguration? Facebook { get; set; }
}

/// <summary>
/// Represents the configuration for the data source.
/// </summary>
public class DataSourceConfiguration
{
    /// <summary>
    /// Gets or sets the database connection string.
    /// </summary>
    public string ConnectionString { get; set; } = string.Empty;
}

/// <summary>
/// Represents the configuration for Upstash.
/// </summary>
public class UpstashConfiguration
{
    /// <summary>
    /// Gets or sets the Upstash host.
    /// </summary>
    public string Host { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the Upstash access token.
    /// </summary>
    public string AccessToken { get; set; } = string.Empty;
}

/// <summary>
/// Represents the configuration for Better Stack.
/// </summary>
public class BetterStackConfiguration
{
    /// <summary>
    /// Gets or sets the Better Stack host.
    /// </summary>
    public string Host { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the Better Stack access token.
    /// </summary>
    public string AccessToken { get; set; } = string.Empty;
}

/// <summary>
/// Represents the configuration for JWT tokens.
/// </summary>
public class JwtTokenConfiguration
{
    /// <summary>
    /// Gets or sets the token issuer.
    /// </summary>
    public string Issuer { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the token audience.
    /// </summary>
    public string Audience { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the secret key used for signing tokens.
    /// </summary>
    public string Secret { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the token expiration time in minutes.
    /// </summary>
    public int Expiration { get; set; }
}

/// <summary>
/// Represents the configuration for ReCaptcha.
/// </summary>
public class ReCaptchaConfiguration
{
    /// <summary>
    /// Gets or sets the ReCaptcha secret key.
    /// </summary>
    public string Secret { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the URL to verify ReCaptcha responses.
    /// </summary>
    public string VerifyUrl { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the minimum required score for a successful verification.
    /// </summary>
    public double MinimumScore { get; set; }
}

/// <summary>
/// Represents the configuration for SMTP email service.
/// </summary>
public class SmtpConfiguration
{
    /// <summary>
    /// Gets or sets the SMTP host address.
    /// </summary>
    public string Host { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the SMTP port number.
    /// </summary>
    public int Port { get; set; }

    /// <summary>
    /// Gets or sets the SMTP username.
    /// </summary>
    public string Username { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the SMTP password.
    /// </summary>
    public string Password { get; set; } = string.Empty;
}

/// <summary>
/// Represents the configuration for OTP generation.
/// </summary>
public class OtpConfiguration
{
    /// <summary>
    /// Gets or sets the OTP expiration time in seconds.
    /// </summary>
    public int Expiration { get; set; }
}

/// <summary>
/// Represents the configuration for user profile management.
/// </summary>
public class ProfileConfiguration
{
    /// <summary>
    /// Gets or sets the time window in seconds during which a password can be reset.
    /// </summary>
    public long PasswordResetWindow { get; set; }
}

/// <summary>
/// Represents the configuration for Cloudinary storage.
/// </summary>
public class CloudinaryConfiguration
{
    /// <summary>
    /// Gets or sets the Cloudinary cloud name.
    /// </summary>
    public string Cloud { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the Cloudinary API key.
    /// </summary>
    public string ApiKey { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the Cloudinary API secret.
    /// </summary>
    public string ApiSecret { get; set; } = string.Empty;
}

/// <summary>
/// Represents the configuration for Facebook authentication.
/// </summary>
public class FacebookConfiguration
{
    /// <summary>
    /// Gets or sets the Facebook App ID.
    /// </summary>
    public string AppId { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the Facebook App Secret.
    /// </summary>
    public string AppSecret { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the Facebook redirect URI.
    /// </summary>
    public string RedirectUri { get; set; } = string.Empty;
}
