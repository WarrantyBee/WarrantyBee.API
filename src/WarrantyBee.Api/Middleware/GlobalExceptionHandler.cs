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
    private readonly ILocalizationService _localizationService;

    /// <summary>
    /// Initializes a new instance of the <see cref="GlobalExceptionHandler"/> class.
    /// </summary>
    /// <param name="next">The next delegate in the request pipeline.</param>
    /// <param name="localizationService">The service used to retrieve localized strings.</param>
    public GlobalExceptionHandler(RequestDelegate next, ILocalizationService localizationService)
    {
        _next = next;
        _localizationService = localizationService;
    }

    /// <summary>
    /// Invokes the middleware asynchronously.
    /// </summary>
    /// <param name="context">The <see cref="HttpContext"/> for the current request.</param>
    /// <returns>A task representing the asynchronous operation.</returns>
    public async Task InvokeAsync(HttpContext context)
    {
        try
        {
            await _next(context);
        }
        catch (Exception ex)
        {
            await HandleExceptionAsync(context, ex);
        }
    }

    private Task HandleExceptionAsync(HttpContext context, Exception exception)
    {
        var statusCode = HttpStatusCode.InternalServerError;
        var errorCode = 1009; // Default unexpected error code
        var message = _localizationService.GetString("UnexpectedError");

        if (exception is ApiException apiException)
        {
            statusCode = apiException.Error.Status;
            errorCode = apiException.Error.Code;
            message = _localizationService.GetString(apiException.Error.Code.ToString());
            
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
