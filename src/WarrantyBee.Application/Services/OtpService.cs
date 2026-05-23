using System.Security.Cryptography;
using WarrantyBee.Shared.Infrastructure.Abstractions;
using WarrantyBee.Application.Abstractions.Services;

namespace WarrantyBee.Application.Services;

/// <summary>
/// Service for generating One-Time Passwords (OTPs).
/// </summary>
public class OtpService : IOtpService
{
    /// <summary>
    /// Generates a random 6-digit OTP.
    /// </summary>
    /// <returns>A string representing the generated OTP.</returns>
    public string Generate()
    {
        var otp = RandomNumberGenerator.GetInt32(0, 1_000_000);
        return otp.ToString("D6");
    }
}
