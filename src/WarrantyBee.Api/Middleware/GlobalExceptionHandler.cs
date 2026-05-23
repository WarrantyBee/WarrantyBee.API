using System.Net;
using WarrantyBee.Application.Abstractions.Services;
using WarrantyBee.Application.Contracts.Common;
using WarrantyBee.Domain.Exceptions;

namespace WarrantyBee.Api.Middleware;

/// <summary>
/// Middleware for handling global exceptions and returning standardized API responses.
/// </summary>
public class GlobalExceptionHandler
{
    private readonly RequestDelegate _next;

    /// <summary>
    /// Initializes a new instance of the <see cref="GlobalExceptionHandler"/> class.
    /// </summary>
    /// <param name="next">The next delegate in the request pipeline.</param>
    public GlobalExceptionHandler(RequestDelegate next)
    {
        _next = next;
    }

    /// <summary>
    /// Invokes the middleware asynchronously.
    /// </summary>
    /// <param name="context">The <see cref="HttpContext"/> for the current request.</param>
    /// <param name="localizationService">The service used to retrieve localized strings (resolved per-request scope).</param>
    /// <returns>A task representing the asynchronous operation.</returns>
    public async Task InvokeAsync(HttpContext context, ILocalizationService localizationService)
    {
        try
        {
            await _next(context);
        }
        catch (Exception ex)
        {
            await HandleExceptionAsync(context, ex, localizationService);
        }
    }

    private Task HandleExceptionAsync(HttpContext context, Exception exception, ILocalizationService localizationService)
    {
        var statusCode = HttpStatusCode.InternalServerError;
        var errorCode = 1009; // Default unexpected error code
        var message = localizationService.GetString("UnexpectedError");

        if (exception is ApiException apiException)
        {
            statusCode = apiException.Error.Status;
            errorCode = apiException.Error.Code;
            message = localizationService.GetString(apiException.Error.Code.ToString());
            
            // Fallback to internal message if translation not found for specific code
            if (message == apiException.Error.Code.ToString())
            {
                message = apiException.Error.Message;
            }
        }

        context.Response.ContentType = "application/json";
        context.Response.StatusCode = (int)statusCode;

        var response = APIResponse<object>.Failure(errorCode, message);
        return context.Response.WriteAsJsonAsync(response);
    }
}
