namespace WarrantyBee.Application.Contracts.Common;

public class APIError
{
    public int Code { get; set; }
    public string Message { get; set; } = string.Empty;

    public APIError() { }
    public APIError(int code, string message)
    {
        Code = code;
        Message = message;
    }
}

public class APIResponse<T>
{
    public T? Data { get; set; }
    public APIError? Error { get; set; }

    public APIResponse() { }
    public APIResponse(T? data) => Data = data;
    public APIResponse(APIError error) => Error = error;
    public APIResponse(T? data, APIError? error)
    {
        Data = data;
        Error = error;
    }

    public static APIResponse<T> Success(T? data) => new(data);
    public static APIResponse<T> Failure(int code, string message) => new(new APIError(code, message));
}
