using System.Net;
using WarrantyBee.Application.Contracts.Common;
using WarrantyBee.Domain.Exceptions;

namespace WarrantyBee.Api.Middleware;

public class GlobalExceptionHandler
{
    private readonly RequestDelegate _next;
    private readonly ILogger<GlobalExceptionHandler> _logger;

    public GlobalExceptionHandler(RequestDelegate next, ILogger<GlobalExceptionHandler> logger)
    {
        _next = next;
        _logger = logger;
    }

    public async Task InvokeAsync(HttpContext context)
    {
        try
        {
            await _next(context);
        }
        catch (ApiException ex)
        {
            await HandleExceptionAsync(context, ex.Error.Status, ex.Error.Code, ex.Message);
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "An unhandled exception occurred.");
            await HandleExceptionAsync(context, HttpStatusCode.InternalServerError, 1009, "An unexpected error occurred.");
        }
    }

    private static Task HandleExceptionAsync(HttpContext context, HttpStatusCode statusCode, int errorCode, string message)
    {
        context.Response.ContentType = "application/json";
        context.Response.StatusCode = (int)statusCode;

        var response = APIResponse<object>.Failure(errorCode, message);
        return context.Response.WriteAsJsonAsync(response);
    }
}
