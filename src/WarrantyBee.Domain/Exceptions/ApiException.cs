using WarrantyBee.Domain.Enums;

namespace WarrantyBee.Domain.Exceptions;

/// <summary>
/// Represents errors that occur during application execution.
/// </summary>
public class ApiException : Exception
{
    /// <summary>
    /// Gets the application error associated with this exception.
    /// </summary>
    public AppError Error { get; }

    /// <summary>
    /// Initializes a new instance of the <see cref="ApiException"/> class with a specified error.
    /// </summary>
    /// <param name="error">The application error.</param>
    public ApiException(AppError error) : base(error.Message)
    {
        Error = error;
    }
}
