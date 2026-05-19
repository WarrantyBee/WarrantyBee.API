using Dapper;
using WarrantyBee.Application.Abstractions.Persistence;

namespace WarrantyBee.Infrastructure.Persistence;

public class OtpRepository : IOtpRepository
{
    private readonly IDbConnectionFactory _connectionFactory;

    public OtpRepository(IDbConnectionFactory connectionFactory)
    {
        _connectionFactory = connectionFactory;
    }

    public async Task<long> StoreAsync(OtpStorageRequest request)
    {
        using var connection = _connectionFactory.CreateConnection();
        var parameters = new DynamicParameters();
        parameters.Add("in_value", request.Value);
        parameters.Add("in_recipient", request.Recipient);
        parameters.Add("in_recipient_id", request.RecipientId);
        parameters.Add("in_type", request.Reason);

        var result = await connection.QueryFirstOrDefaultAsync<dynamic>("CALL usp_StoreOtp(@in_value, @in_recipient, @in_recipient_id, @in_type)", parameters);
        return result != null ? (long)result.id : 0;
    }

    public async Task<string?> GetAsync(OtpSearchFilter filter)
    {
        using var connection = _connectionFactory.CreateConnection();
        var parameters = new DynamicParameters();
        parameters.Add("in_recipient", filter.Recipient);
        parameters.Add("in_recipient_id", filter.RecipientId);
        parameters.Add("in_type", filter.Reason);

        // Java code expects multiple result sets. 
        // Result 0: Status, Result 1: Data
        using var multi = await connection.QueryMultipleAsync("CALL usp_GetOtp(@in_recipient, @in_recipient_id, @in_type)", parameters);
        _ = await multi.ReadFirstOrDefaultAsync<dynamic>(); // Result 0: Status
        var dataRow = await multi.ReadFirstOrDefaultAsync<dynamic>(); // Result 1: Data
        
        return dataRow != null ? (string)dataRow.value : null;
    }
}
