using System.Text.Json.Serialization;
using WarrantyBee.Application.Contracts.Common;
using WarrantyBee.Application.Contracts.Users;

namespace WarrantyBee.Application.Contracts.Identity;

public class SignUpResponse
{
    public long Id { get; set; }

    public SignUpResponse() { }
    public SignUpResponse(long id) => Id = id;
}

[JsonDerivedType(typeof(LoginResponse), typeDiscriminator: "login")]
[JsonDerivedType(typeof(MFALoginResponse), typeDiscriminator: "mfa")]
public interface ILoginResponse { }

public class LoginResponse : ILoginResponse
{
    public string AccessToken { get; set; } = string.Empty;
    public string IssuedAt { get; set; } = string.Empty;
    public string ExpiresAt { get; set; } = string.Empty;
    public UserResponse? User { get; set; }

    public LoginResponse() { }
    public LoginResponse(string accessToken, string issuedAt, string expiresAt, UserResponse user)
    {
        AccessToken = accessToken;
        IssuedAt = issuedAt;
        ExpiresAt = expiresAt;
        User = user;
    }
}

public class MFALoginResponse : ILoginResponse
{
    public string LoginToken { get; set; } = string.Empty;

    public MFALoginResponse() { }
    public MFALoginResponse(string loginToken) => LoginToken = loginToken;
}

public class ForgotPasswordRequest
{
    public string Email { get; set; } = string.Empty;
    public string? CaptchaResponse { get; set; }
}

public class ResetPasswordRequest
{
    public string Otp { get; set; } = string.Empty;
    public string Email { get; set; } = string.Empty;
    public string NewPassword { get; set; } = string.Empty;
    public string? CaptchaResponse { get; set; }
}
