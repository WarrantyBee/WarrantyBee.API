using System.Security.Cryptography;
using WarrantyBee.Application.Abstractions.Services;

namespace WarrantyBee.Application.Services;

public class OtpService : IOtpService
{
    public string Generate()
    {
        var otp = RandomNumberGenerator.GetInt32(0, 1_000_000);
        return otp.ToString("D6");
    }
}
