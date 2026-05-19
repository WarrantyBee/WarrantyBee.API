using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;
using Microsoft.Extensions.Options;
using Microsoft.IdentityModel.Tokens;
using WarrantyBee.Application.Abstractions.Services;
using WarrantyBee.Application.Configuration;

namespace WarrantyBee.Infrastructure.Services;

/// <summary>
/// Provides services for generating and validating JSON Web Tokens (JWT).
/// </summary>
public class TokenService : ITokenService
{
    private readonly JwtTokenConfiguration _config;

    /// <summary>
    /// Initializes a new instance of the <see cref="TokenService"/> class.
    /// </summary>
    /// <param name="config">The application configuration containing JWT settings.</param>
    /// <exception cref="ArgumentNullException">Thrown when configuration or JWT settings are missing.</exception>
    public TokenService(IOptions<AppConfiguration> config)
    {
        _config = config.Value.Jwt ?? throw new ArgumentNullException(nameof(config));
    }

    /// <summary>
    /// Generates a JWT based on the provided claims.
    /// </summary>
    /// <param name="claims">A dictionary of claims to include in the token.</param>
    /// <returns>A signed JWT string.</returns>
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

    /// <summary>
    /// Validates a JWT and returns its claims.
    /// </summary>
    /// <param name="token">The JWT string to validate.</param>
    /// <returns>A dictionary of claims extracted from the token.</returns>
    /// <exception cref="Exception">Thrown when the token is invalid or expired.</exception>
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
