namespace WarrantyBee.Application.Configuration;

public class AppConfiguration
{
    public string? Name { get; set; }
    public string? Environment { get; set; }
    public bool IsCaptchaEnabled { get; set; }
    public DataSourceConfiguration? DataSource { get; set; }
    public UpstashConfiguration? Upstash { get; set; }
    public BetterStackConfiguration? BetterStack { get; set; }
    public JwtTokenConfiguration? Jwt { get; set; }
    public ReCaptchaConfiguration? ReCaptcha { get; set; }
    public SmtpConfiguration? Smtp { get; set; }
    public OtpConfiguration? Otp { get; set; }
    public ProfileConfiguration? Profile { get; set; }
    public CloudinaryConfiguration? Cloudinary { get; set; }
    public FacebookConfiguration? Facebook { get; set; }
}

public class DataSourceConfiguration
{
    public string ConnectionString { get; set; } = string.Empty;
}

public class UpstashConfiguration
{
    public string Host { get; set; } = string.Empty;
    public string AccessToken { get; set; } = string.Empty;
}

public class BetterStackConfiguration
{
    public string Host { get; set; } = string.Empty;
    public string AccessToken { get; set; } = string.Empty;
}

public class JwtTokenConfiguration
{
    public string Issuer { get; set; } = string.Empty;
    public string Audience { get; set; } = string.Empty;
    public string Secret { get; set; } = string.Empty;
    public int Expiration { get; set; }
}

public class ReCaptchaConfiguration
{
    public string Secret { get; set; } = string.Empty;
    public string VerifyUrl { get; set; } = string.Empty;
    public double MinimumScore { get; set; }
}

public class SmtpConfiguration
{
    public string Host { get; set; } = string.Empty;
    public int Port { get; set; }
    public string Username { get; set; } = string.Empty;
    public string Password { get; set; } = string.Empty;
}

public class OtpConfiguration
{
    public int Expiration { get; set; }
}

public class ProfileConfiguration
{
    public long PasswordResetWindow { get; set; }
}

public class CloudinaryConfiguration
{
    public string Cloud { get; set; } = string.Empty;
    public string ApiKey { get; set; } = string.Empty;
    public string ApiSecret { get; set; } = string.Empty;
}

public class FacebookConfiguration
{
    public string AppId { get; set; } = string.Empty;
    public string AppSecret { get; set; } = string.Empty;
    public string RedirectUri { get; set; } = string.Empty;
}
