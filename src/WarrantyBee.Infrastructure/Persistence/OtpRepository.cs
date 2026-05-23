using WarrantyBee.Shared.Infrastructure.Abstractions;
using Dapper;
using WarrantyBee.Application.Abstractions.Persistence;

namespace WarrantyBee.API.Infrastructure.Persistence;

/// <summary>
/// Repository for managing OTP (One-Time Password) data in the database.
/// </summary>
public class OtpRepository : IOtpRepository
{
    private readonly IDbConnectionFactory _connectionFactory;

    /// <summary>
    /// Initializes a new instance of the <see cref="OtpRepository"/> class.
    /// </summary>
    /// <param name="connectionFactory">The factory used to create database connections.</param>
    public OtpRepository(IDbConnectionFactory connectionFactory)
    {
        _connectionFactory = connectionFactory;
    }

    /// <summary>
    /// Stores a new OTP in the database.
    /// </summary>
    /// <param name="request">The OTP storage request details.</param>
    /// <returns>The ID of the stored OTP record.</returns>
    public async Task<long> StoreAsync(OtpStorageRequest request)
    {
        using var connection = _connectionFactory.CreateConnection();
        var parameters = new DynamicParameters();
        parameters.Add("in_value", request.Value);
        parameters.Add("in_recipient", request.Recipient);
        parameters.Add("in_recipient_id", request.RecipientId);
        parameters.Add("in_type", request.Reason);

        var result = await connection.QueryFirstOrDefaultAsync<dynamic>("EXEC dbo.usp_StoreOtp @in_value, @in_recipient, @in_recipient_id, @in_type", parameters);
        return result != null ? (long)result.id : 0;
    }

    /// <summary>
    /// Retrieves an OTP from the database based on the provided filter.
    /// </summary>
    /// <param name="filter">The filter criteria for searching the OTP.</param>
    /// <returns>The OTP value if found; otherwise, null.</returns>
    public async Task<string?> GetAsync(OtpSearchFilter filter)
    {
        using var connection = _connectionFactory.CreateConnection();
        var parameters = new DynamicParameters();
        parameters.Add("in_recipient", filter.Recipient);
        parameters.Add("in_recipient_id", filter.RecipientId);
        parameters.Add("in_type", filter.Reason);

        // Java code expects multiple result sets. 
        // Result 0: Status, Result 1: Data
        using var multi = await connection.QueryMultipleAsync("EXEC dbo.usp_GetOtp @in_recipient, @in_recipient_id, @in_type", parameters);
        _ = await multi.ReadFirstOrDefaultAsync<dynamic>(); // Result 0: Status
        var dataRow = await multi.ReadFirstOrDefaultAsync<dynamic>(); // Result 1: Data
        
        return dataRow != null ? (string)dataRow.value : null;
    }
}

