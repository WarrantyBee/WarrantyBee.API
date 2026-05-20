using System.Data;
using Microsoft.Data.SqlClient;
using Microsoft.Extensions.Options;
using WarrantyBee.Application.Configuration;

namespace WarrantyBee.Infrastructure.Persistence;

/// <summary>
/// Defines a factory for creating database connections.
/// </summary>
public interface IDbConnectionFactory
{
    /// <summary>
    /// Creates a new database connection.
    /// </summary>
    /// <returns>An <see cref="IDbConnection"/> instance.</returns>
    IDbConnection CreateConnection();
}

/// <summary>
/// Implementation of <see cref="IDbConnectionFactory"/> that creates SQL Server connections.
/// </summary>
public class DbConnectionFactory : IDbConnectionFactory
{
    private readonly AppConfiguration _config;

    /// <summary>
    /// Initializes a new instance of the <see cref="DbConnectionFactory"/> class.
    /// </summary>
    /// <param name="config">The application configuration containing the connection string.</param>
    public DbConnectionFactory(IOptions<AppConfiguration> config)
    {
        _config = config.Value;
    }

    /// <summary>
    /// Creates a new <see cref="SqlConnection"/> based on the configured connection string.
    /// </summary>
    /// <returns>A new <see cref="IDbConnection"/> instance.</returns>
    public IDbConnection CreateConnection()
    {
        return new SqlConnection(_config.DataSource?.ConnectionString);
    }
}
