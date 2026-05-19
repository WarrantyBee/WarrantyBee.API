using System.Data;
using MySqlConnector;
using Microsoft.Extensions.Options;
using WarrantyBee.Application.Configuration;

namespace WarrantyBee.Infrastructure.Persistence;

public interface IDbConnectionFactory
{
    IDbConnection CreateConnection();
}

public class DbConnectionFactory : IDbConnectionFactory
{
    private readonly AppConfiguration _config;

    public DbConnectionFactory(IOptions<AppConfiguration> config)
    {
        _config = config.Value;
    }

    public IDbConnection CreateConnection()
    {
        return new MySqlConnection(_config.DataSource?.ConnectionString);
    }
}
