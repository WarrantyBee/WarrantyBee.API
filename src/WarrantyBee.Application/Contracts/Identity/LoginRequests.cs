using WarrantyBee.Application.Contracts.Common;

namespace WarrantyBee.Application.Contracts.Identity;

public abstract class LoginRequest : BaseRequest
{
    public string Email { get; set; } = string.Empty;
}

public class SimpleLoginRequest : LoginRequest
{
    public string? Password { get; set; }
    public byte? AuthProvider { get; set; }
    public string? AuthProviderUserId { get; set; }
}

public class MFALoginRequest : LoginRequest
{
    public string? Token { get; set; }
    public string? Otp { get; set; }
}
