using FluentAssertions;
using WarrantyBee.Application.Services;
using Xunit;

namespace WarrantyBee.Tests.Services;

public class OtpServiceTests
{
    private readonly OtpService _service;

    public OtpServiceTests()
    {
        _service = new OtpService();
    }

    [Fact]
    public void Generate_ReturnsSixDigitString()
    {
        var otp = _service.Generate();
        otp.Should().HaveLength(6);
        int.TryParse(otp, out _).Should().BeTrue();
    }

    [Fact]
    public void Generate_ReturnsDifferentValues()
    {
        var otp1 = _service.Generate();
        var otp2 = _service.Generate();
        otp1.Should().NotBe(otp2);
    }
}

