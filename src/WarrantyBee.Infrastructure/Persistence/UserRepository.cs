using WarrantyBee.Shared.Infrastructure.Abstractions;
using System.Data;
using Dapper;
using WarrantyBee.Application.Abstractions.Persistence;
using WarrantyBee.Application.Contracts.Identity;
using WarrantyBee.Application.Contracts.Users;
using WarrantyBee.Application.Contracts.Geographic;
using WarrantyBee.Shared.Core.Enums;

namespace WarrantyBee.API.Infrastructure.Persistence;

/// <summary>
/// Repository for managing user data in the database, including authentication tokens and profile updates.
/// </summary>
public class UserRepository : IUserRepository
{
    private readonly IDbConnectionFactory _connectionFactory;

    /// <summary>
    /// Initializes a new instance of the <see cref="UserRepository"/> class.
    /// </summary>
    /// <param name="connectionFactory">The factory used to create database connections.</param>
    public UserRepository(IDbConnectionFactory connectionFactory)
    {
        _connectionFactory = connectionFactory;
    }

    /// <summary>
    /// Registers a new user in the database.
    /// </summary>
    /// <param name="request">The sign-up request details.</param>
    /// <returns>The ID of the newly created user.</returns>
    public async Task<long> CreateAsync(SignUpRequest request)
    {
        using var connection = _connectionFactory.CreateConnection();
        var parameters = new DynamicParameters();
        parameters.Add("in_firstname", request.Firstname);
        parameters.Add("in_lastname", request.Lastname);
        parameters.Add("in_email", request.Email);
        parameters.Add("in_password", request.Password);
        parameters.Add("in_accepted_tnc", request.HasAcceptedTermsAndConditions);
        parameters.Add("in_accepted_pp", request.HasAcceptedPrivacyPolicy);
        parameters.Add("in_phone_code", request.PhoneCode);
        parameters.Add("in_phone_number", request.PhoneNumber);
        parameters.Add("in_gender", request.Gender);
        parameters.Add("in_date_of_birth", request.DateOfBirth);
        parameters.Add("in_address_line1", request.AddressLine1);
        parameters.Add("in_address_line2", request.AddressLine2);
        parameters.Add("in_country_id", request.CountryId);
        parameters.Add("in_region_id", request.RegionId);
        parameters.Add("in_city", request.City);
        parameters.Add("in_postal_code", request.PostalCode);
        parameters.Add("in_avatar_url", request.AvatarUrl);
        parameters.Add("in_culture_id", request.CultureId);
        parameters.Add("in_auth_provider", request.AuthProvider);
        parameters.Add("in_auth_provider_user_id", request.AuthProviderUserId);

        var result = await connection.QueryAsync<long?>("EXEC dbo.usp_RegisterUser @in_firstname, @in_lastname, @in_email, @in_password, @in_accepted_tnc, @in_accepted_pp, @in_phone_code, @in_phone_number, @in_gender, @in_date_of_birth, @in_address_line1, @in_address_line2, @in_country_id, @in_region_id, @in_city, @in_postal_code, @in_avatar_url, @in_culture_id, @in_auth_provider, @in_auth_provider_user_id", parameters);
        return result.FirstOrDefault() ?? 0;
    }

    /// <summary>
    /// Creates a new user with administrative privileges, allowing role specification.
    /// </summary>
    public async Task<long> AdminCreateUserAsync(AdminCreateUserRequest request)
    {
        using var connection = _connectionFactory.CreateConnection();
        var parameters = new DynamicParameters();
        parameters.Add("in_firstname", request.Firstname);
        parameters.Add("in_lastname", request.Lastname);
        parameters.Add("in_email", request.Email);
        parameters.Add("in_password", request.Password);
        parameters.Add("in_role_id", request.RoleId);
        parameters.Add("in_phone_code", request.PhoneCode);
        parameters.Add("in_phone_number", request.PhoneNumber);
        parameters.Add("in_gender", request.Gender);
        parameters.Add("in_country_id", request.CountryId);
        parameters.Add("in_region_id", request.RegionId);
        parameters.Add("in_city", request.City);
        parameters.Add("in_postal_code", request.PostalCode);
        parameters.Add("in_culture_id", request.CultureId);

        var result = await connection.QueryAsync<long?>("EXEC dbo.usp_AdminCreateUser @in_firstname, @in_lastname, @in_email, @in_password, @in_role_id, @in_phone_code, @in_phone_number, @in_gender, @in_country_id, @in_region_id, @in_city, @in_postal_code, @in_culture_id", parameters);
        return result.FirstOrDefault() ?? 0;
    }

    /// <summary>
    /// Retrieves a user from the database based on the provided filter.
    /// </summary>
    /// <param name="filter">The filter criteria for searching the user.</param>
    /// <returns>A <see cref="UserResponse"/> containing user details if found; otherwise, null.</returns>
    public async Task<UserResponse?> GetAsync(UserSearchFilter filter)
    {
        using var connection = _connectionFactory.CreateConnection();
        var parameters = new DynamicParameters();
        parameters.Add("in_id", filter.Id);
        parameters.Add("in_email", filter.Email);

        using var multi = await connection.QueryMultipleAsync("EXEC dbo.usp_GetUser @in_id, @in_email", parameters);
        var statusRow = await multi.ReadFirstOrDefaultAsync<dynamic>();
        
        if (statusRow == null || (int)statusRow.status != 0) return null;
        
        var userRow = await multi.ReadFirstOrDefaultAsync<dynamic>();
        if (userRow == null) return null;

        var user = new UserResponse
        {
            Id = Convert.ToInt64(userRow.id),
            Firstname = userRow.firstname?.ToString(),
            Lastname = userRow.lastname?.ToString(),
            Email = userRow.email?.ToString(),
            Password = userRow.password?.ToString(),
            AuthProvider = userRow.auth_provider != null ? Convert.ToByte(userRow.auth_provider) : (byte?)null,
            AuthProviderUserId = userRow.auth_provider_user_id?.ToString(),
            Profile = new UserProfileResponse
            {
                PhoneCode = userRow.phone_code?.ToString(),
                PhoneNumber = userRow.phone_number?.ToString(),
                Gender = userRow.gender != null ? Convert.ToByte(userRow.gender) : (byte?)null,
                DateOfBirth = userRow.date_of_birth,
                AvatarUrl = userRow.avatar_url?.ToString(),
                Address = new UserAddressResponse
                {
                    AddressLine1 = userRow.address_line1?.ToString(),
                    AddressLine2 = userRow.address_line2?.ToString(),
                    City = userRow.city?.ToString(),
                    PostalCode = userRow.postal_code?.ToString(),
                    Country = userRow.country_id != null ? new CountryResponse(
                        Convert.ToInt64(userRow.country_id), userRow.country_name?.ToString() ?? string.Empty, string.Empty, userRow.country_iso3?.ToString() ?? string.Empty, string.Empty, string.Empty, string.Empty, string.Empty) : null,
                    Region = userRow.region_id != null ? new RegionResponse(
                        Convert.ToInt64(userRow.region_id), userRow.region_name?.ToString() ?? string.Empty, userRow.region_iso?.ToString() ?? string.Empty, string.Empty, userRow.timezone_id != null ? Convert.ToInt64(userRow.timezone_id) : 0) : null
                },
                Timezone = userRow.timezone_id != null ? new TimeZoneResponse(
                    Convert.ToInt64(userRow.timezone_id), userRow.timezone_name?.ToString() ?? string.Empty, userRow.timezone_abbreviation?.ToString() ?? string.Empty, 
                    Convert.ToInt16(userRow.timezone_utc_offset_minutes), Convert.ToBoolean(userRow.timezone_observes_dst), Convert.ToInt16(userRow.timezone_current_offset_minutes)) : null,
                Currency = userRow.currency_id != null ? new CurrencyResponse(
                    Convert.ToInt64(userRow.currency_id), userRow.currency_name?.ToString() ?? string.Empty, userRow.currency_iso_code?.ToString() ?? string.Empty, userRow.currency_code?.ToString() ?? string.Empty, 
                    userRow.currency_symbol?.ToString() ?? string.Empty, Convert.ToByte(userRow.currency_minor_unit)) : null,
                Culture = userRow.culture_id != null ? new CultureResponse(
                    Convert.ToInt64(userRow.culture_id), userRow.culture_iso_code?.ToString() ?? string.Empty, Convert.ToBoolean(userRow.culture_rtl),
                    new LanguageResponse(Convert.ToInt64(userRow.language_id), userRow.language_name?.ToString() ?? string.Empty, userRow.language_iso_code?.ToString() ?? string.Empty, userRow.language_native_name?.ToString() ?? string.Empty)) : null,
                Settings = new UserSettingsResponse
                {
                    Is2FAEnabled = Convert.ToBoolean(userRow.is_2fa_enabled),
                    PasswordUpdatedAt = userRow.password_updated_at
                }
            },
            AuthorizationContext = new UserAuthorization
            {
                Role = userRow.role == null ? SecurityRole.None : (Enum.TryParse<SecurityRole>(userRow.role.ToString(), true, out SecurityRole role) ? role : SecurityRole.None),
                Permissions = ParsePermissions(userRow.permissions?.ToString())
            }
        };

        return user;
    }

    /// <summary>
    /// Stores a login token for a user.
    /// </summary>
    /// <param name="details">The details of the token and the user.</param>
    /// <returns>True if the token was successfully stored; otherwise, false.</returns>
    public async Task<bool> StoreTokenAsync(LoginTokenDetails details)
    {
        using var connection = _connectionFactory.CreateConnection();
        var parameters = new DynamicParameters();
        parameters.Add("in_user_id", details.UserId);
        parameters.Add("in_token", details.Token);

        var result = await connection.QueryFirstOrDefaultAsync<dynamic>("EXEC dbo.usp_StoreLoginToken @in_user_id, @in_token", parameters);
        return result != null && result.status == 0;
    }

    /// <summary>
    /// Validates a login token for a user.
    /// </summary>
    /// <param name="details">The details of the token and the user to validate.</param>
    /// <returns>True if the token is valid; otherwise, false.</returns>
    public async Task<bool> ValidateTokenAsync(LoginTokenDetails details)
    {
        using var connection = _connectionFactory.CreateConnection();
        return await connection.ExecuteScalarAsync<bool>("SELECT dbo.ufn_ValidateLoginToken(@UserId, @Token)", new { details.UserId, details.Token });
    }

    /// <summary>
    /// Resets a user's password.
    /// </summary>
    /// <param name="request">The password reset request details.</param>
    /// <returns>True if the password was successfully reset; otherwise, false.</returns>
    public async Task<bool> ResetPasswordAsync(PasswordResetRequest request)
    {
        using var connection = _connectionFactory.CreateConnection();
        var result = await connection.QueryFirstOrDefaultAsync<dynamic>("EXEC dbo.usp_ResetPassword @in_user_id, @in_new_password", new { in_user_id = request.UserId, in_new_password = request.NewPassword });
        return result != null && result.status == 0;
    }

    /// <summary>
    /// Retrieves the historical passwords for a user.
    /// </summary>
    /// <param name="userId">The ID of the user.</param>
    /// <returns>A collection of historical password hashes.</returns>
    public async Task<IEnumerable<string>> GetPasswordsAsync(long userId)
    {
        using var connection = _connectionFactory.CreateConnection();
        return await connection.QueryAsync<string>("EXEC dbo.usp_GetUserPasswords @in_user_id", new { in_user_id = userId });
    }

    /// <summary>
    /// Updates a user's profile information.
    /// </summary>
    /// <param name="request">The profile update request details.</param>
    /// <returns>True if the profile was successfully updated; otherwise, false.</returns>
    public async Task<bool> UpdateProfileAsync(ProfileUpdateRequest request)
    {
        using var connection = _connectionFactory.CreateConnection();
        var parameters = new DynamicParameters();
        parameters.Add("in_user_id", request.UserId);
        parameters.Add("in_address_line1", request.AddressLine1);
        parameters.Add("in_address_line2", request.AddressLine2);
        parameters.Add("in_phone_code", request.PhoneCode);
        parameters.Add("in_phone_number", request.PhoneNumber);
        parameters.Add("in_country_id", request.CountryId);
        parameters.Add("in_region_id", request.RegionId);
        parameters.Add("in_culture_id", request.CultureId);
        parameters.Add("in_city", request.City);
        parameters.Add("in_postal_code", request.PostalCode);
        parameters.Add("in_avatar_url", request.AvatarUrl);

        var result = await connection.QueryFirstOrDefaultAsync<dynamic>("EXEC dbo.usp_UpdateUserProfile @in_user_id, @in_address_line1, @in_address_line2, @in_phone_code, @in_phone_number, @in_country_id, @in_region_id, @in_culture_id, @in_city, @in_postal_code, @in_avatar_url", parameters);
        return result != null && result.status == 1;
    }

    private IEnumerable<SecurityPermission> ParsePermissions(string? permissions)
    {
        if (string.IsNullOrWhiteSpace(permissions)) return [];
        return permissions.Split(',')
            .Select(p => Enum.TryParse<SecurityPermission>(p.Trim(), true, out var perm) ? perm : SecurityPermission.None)
            .Where(p => p != SecurityPermission.None);
    }
}

