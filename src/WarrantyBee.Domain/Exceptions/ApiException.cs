using WarrantyBee.Domain.Enums;

namespace WarrantyBee.Domain.Exceptions;

public class ApiException : Exception
{
    public AppError Error { get; }

    public ApiException(AppError error) : base(error.Message)
    {
        Error = error;
    }
}
