using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;
using Microsoft.Extensions.Options;
using Microsoft.IdentityModel.Tokens;
using WarrantyBee.Application.Abstractions.Services;
using WarrantyBee.Application.Configuration;

namespace WarrantyBee.Infrastructure.Services;

public class TokenService : ITokenService
{
    private readonly JwtTokenConfiguration _config;

    public TokenService(IOptions<AppConfiguration> config)
    {
        _config = config.Value.Jwt ?? throw new ArgumentNullException(nameof(config));
    }

    public string Generate(IDictionary<string, object> claims)
    {
        var securityKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_config.Secret));
        var credentials = new SigningCredentials(securityKey, SecurityAlgorithms.HmacSha256);

        var tokenClaims = claims.Select(c => new Claim(c.Key, c.Value.ToString() ?? string.Empty)).ToList();
        
        // Add standard claims if not present
        if (!tokenClaims.Any(c => c.Type == JwtRegisteredClaimNames.Iss))
            tokenClaims.Add(new Claim(JwtRegisteredClaimNames.Iss, _config.Issuer));
        if (!tokenClaims.Any(c => c.Type == JwtRegisteredClaimNames.Aud))
            tokenClaims.Add(new Claim(JwtRegisteredClaimNames.Aud, _config.Audience));

        var token = new JwtSecurityToken(
            issuer: _config.Issuer,
            audience: _config.Audience,
            claims: tokenClaims,
            expires: DateTime.UtcNow.AddMinutes(_config.Expiration),
            signingCredentials: credentials);

        return new JwtSecurityTokenHandler().WriteToken(token);
    }

    public IDictionary<string, object> Validate(string token)
    {
        var tokenHandler = new JwtSecurityTokenHandler();
        var key = Encoding.UTF8.GetBytes(_config.Secret);

        try
        {
            tokenHandler.ValidateToken(token, new TokenValidationParameters
            {
                ValidateIssuerSigningKey = true,
                IssuerSigningKey = new SymmetricSecurityKey(key),
                ValidateIssuer = true,
                ValidIssuer = _config.Issuer,
                ValidateAudience = true,
                ValidAudience = _config.Audience,
                ValidateLifetime = true,
                ClockSkew = TimeSpan.Zero
            }, out SecurityToken validatedToken);

            var jwtToken = (JwtSecurityToken)validatedToken;
            var claims = jwtToken.Claims.ToDictionary(c => c.Type, c => (object)c.Value);
            
            // Add iat and exp as per Java implementation expectations
            claims["iat"] = jwtToken.IssuedAt;
            claims["exp"] = jwtToken.ValidTo;

            return claims;
        }
        catch (Exception ex)
        {
            throw new Exception("Invalid token", ex);
        }
    }
}
