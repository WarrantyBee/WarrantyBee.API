using System.Net;

namespace WarrantyBee.Domain.Enums;

/// <summary>
/// Represents an error in the application.
/// </summary>
public class AppError
{
    /// <summary>
    /// Gets or sets the error code.
    /// </summary>
    public int Code { get; set; }

    /// <summary>
    /// Gets or sets the error message.
    /// </summary>
    public string Message { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the HTTP status code associated with the error.
    /// </summary>
    public HttpStatusCode Status { get; set; }

    /// <summary>
    /// Initializes a new instance of the <see cref="AppError"/> class.
    /// </summary>
    public AppError() { }

    /// <summary>
    /// Initializes a new instance of the <see cref="AppError"/> class with specified code, message and status.
    /// </summary>
    /// <param name="code">The error code.</param>
    /// <param name="message">The error message.</param>
    /// <param name="status">The HTTP status code.</param>
    public AppError(int code, string message, HttpStatusCode status)
    {
        Code = code;
        Message = message;
        Status = status;
    }
}

/// <summary>
/// Provides a collection of predefined application errors.
/// </summary>
public static class Errors
{
    /// <summary>
    /// Error for invalid input.
    /// </summary>
    public static readonly AppError InvalidInput = new(1001, "Invalid input provided.", HttpStatusCode.BadRequest);
    public static readonly AppError InvalidLoginCredentials = new(1002, "Invalid email or password.", HttpStatusCode.BadRequest);
    public static readonly AppError Unauthenticated = new(1003, "Missing or invalid authentication token.", HttpStatusCode.Unauthorized);
    public static readonly AppError UnauthorizedAccess = new(1004, "User does not have permission to access this resource.", HttpStatusCode.Forbidden);
    public static readonly AppError ResourceNotFound = new(1005, "The requested resource was not found.", HttpStatusCode.NotFound);
    public static readonly AppError UserNotFound = new(1006, "The specified user was not found.", HttpStatusCode.NotFound);
    public static readonly AppError ResourceAlreadyExists = new(1007, "This resource already exists.", HttpStatusCode.Conflict);
    public static readonly AppError EmailAlreadyExists = new(1008, "An account with this email already exists.", HttpStatusCode.Conflict);
    public static readonly AppError InternalServerError = new(1009, "An unexpected error occurred.", HttpStatusCode.InternalServerError);
    public static readonly AppError CacheError = new(1010, "There was an error with the cache service.", HttpStatusCode.InternalServerError);
    public static readonly AppError TelemetryServiceError = new(1011, "There was an error with the telemetry service.", HttpStatusCode.InternalServerError);
    public static readonly AppError CaptchaServiceError = new(1012, "There was an error with the captcha service.", HttpStatusCode.InternalServerError);
    public static readonly AppError InvalidToken = new(1013, "The provided token is invalid.", HttpStatusCode.Unauthorized);
    public static readonly AppError JwtGenerationError = new(1014, "Could not generate JWT token.", HttpStatusCode.InternalServerError);
    public static readonly AppError InvalidCaptcha = new(1015, "Given captcha is expired.", HttpStatusCode.BadRequest);
    public static readonly AppError UserNotRegistered = new(1016, "The specified user is not registered.", HttpStatusCode.NotFound);
    public static readonly AppError UserAlreadyRegistered = new(1017, "The specified user is already registered.", HttpStatusCode.Conflict);
    public static readonly AppError RequestBodyEmpty = new(1018, "Request body is empty.", HttpStatusCode.BadRequest);
    public static readonly AppError CaptchaResponseRequired = new(1019, "Captcha response is required.", HttpStatusCode.BadRequest);
    public static readonly AppError FirstnameRequired = new(1020, "Firstname is required.", HttpStatusCode.BadRequest);
    public static readonly AppError LastnameRequired = new(1021, "Lastname is required.", HttpStatusCode.BadRequest);
    public static readonly AppError EmailRequired = new(1022, "Email is required.", HttpStatusCode.BadRequest);
    public static readonly AppError PasswordRequired = new(1023, "Password is required.", HttpStatusCode.BadRequest);
    public static readonly AppError AddressRequired = new(1024, "Address is required.", HttpStatusCode.BadRequest);
    public static readonly AppError CityRequired = new(1025, "City is required.", HttpStatusCode.BadRequest);
    public static readonly AppError PostalCodeRequired = new(1026, "Postal code is required.", HttpStatusCode.BadRequest);
    public static readonly AppError InvalidEmail = new(1027, "Invalid email format.", HttpStatusCode.BadRequest);
    public static readonly AppError InvalidGenderValue = new(1028, "Invalid gender value.", HttpStatusCode.BadRequest);
    public static readonly AppError UserIsMinor = new(1029, "User must be at least 18 years old.", HttpStatusCode.BadRequest);
    public static readonly AppError StrongPasswordRequired = new(1030, "Password is not strong enough.", HttpStatusCode.BadRequest);
    public static readonly AppError PhoneNumberRequired = new(1031, "Phone number is required.", HttpStatusCode.BadRequest);
    public static readonly AppError UserRegistrationFailed = new(1032, "User registration failed.", HttpStatusCode.InternalServerError);
    public static readonly AppError OtpRecipientRequired = new(1033, "The OTP recipient is required.", HttpStatusCode.BadRequest);
    public static readonly AppError InvalidOtp = new(1034, "Invalid OTP.", HttpStatusCode.BadRequest);
    public static readonly AppError OtpExpired = new(1035, "OTP has expired.", HttpStatusCode.BadRequest);
    public static readonly AppError MaxOtpAttemptsReached = new(1036, "Maximum OTP attempts reached.", HttpStatusCode.BadRequest);
    public static readonly AppError OtpGenerationFailed = new(1039, "Could not generate OTP.", HttpStatusCode.InternalServerError);
    public static readonly AppError EmailTemplateNotFound = new(1037, "Email template not found.", HttpStatusCode.NotFound);
    public static readonly AppError EmailTemplateParsingError = new(1038, "There was an error parsing the email template.", HttpStatusCode.InternalServerError);
    public static readonly AppError TemplateMacroCouldNotBeResolved = new(1040, "Could not resolve all macros in the email template.", HttpStatusCode.InternalServerError);
    public static readonly AppError RateLimitExceeded = new(1041, "Rate limit exceeded. Please try again later.", HttpStatusCode.TooManyRequests);
    public static readonly AppError OtpRequired = new(1042, "The OTP is required.", HttpStatusCode.BadRequest);
    public static readonly AppError InvalidOtpRecipient = new(1043, "The OTP recipient is invalid.", HttpStatusCode.BadRequest);
    public static readonly AppError LoginTokenCouldNotBeSaved = new(1044, "The login token could not be saved.", HttpStatusCode.InternalServerError);
    public static readonly AppError MfaNotEnabled = new(1045, "The user has not enabled MFA.", HttpStatusCode.BadRequest);
    public static readonly AppError InvalidRequestBody = new(1046, "The request body is invalid.", HttpStatusCode.BadRequest);
    public static readonly AppError TokenRequired = new(1047, "The token is required and it cannot be empty.", HttpStatusCode.BadRequest);
    public static readonly AppError PasswordResetFailed = new(1048, "Password could not be reset.", HttpStatusCode.InternalServerError);
    public static readonly AppError PasswordRecentlyUpdated = new(1049, "Password was recently updated. Please try again later.", HttpStatusCode.TooManyRequests);
    public static readonly AppError PasswordAlreadyUsed = new(1050, "The specified password was previously used.", HttpStatusCode.BadRequest);
    public static readonly AppError CountryRequired = new(1051, "The country is required.", HttpStatusCode.BadRequest);
    public static readonly AppError RegionRequired = new(1052, "The region is required.", HttpStatusCode.BadRequest);
    public static readonly AppError CultureRequired = new(1053, "The culture is required.", HttpStatusCode.BadRequest);
    public static readonly AppError SessionExpired = new(1054, "The session has expired. Please log in again.", HttpStatusCode.Unauthorized);
    public static readonly AppError InvalidAuthHeader = new(1055, "Missing or invalid Authorization header.", HttpStatusCode.Unauthorized);
    public static readonly AppError InvalidExpiredToken = new(1056, "Invalid or expired Authentication token.", HttpStatusCode.Unauthorized);
    public static readonly AppError TermsAndConditionsNotAccepted = new(1057, "The terms and conditions are not accepted.", HttpStatusCode.BadRequest);
    public static readonly AppError PrivacyPolicyNotAccepted = new(1058, "The privacy policy is not accepted.", HttpStatusCode.BadRequest);
    public static readonly AppError PhoneCodeRequired = new(1059, "The phone code is required.", HttpStatusCode.BadRequest);
    public static readonly AppError InvalidPhoneCode = new(1060, "The phone code is invalid.", HttpStatusCode.BadRequest);
    public static readonly AppError FileIsEmpty = new(1061, "The uploaded file is empty.", HttpStatusCode.BadRequest);
    public static readonly AppError FileExceededAllowedSize = new(1062, "The uploaded file has exceeded the allowed size.", HttpStatusCode.BadRequest);
    public static readonly AppError InvalidFileFormat = new(1063, "The uploaded file has an invalid format.", HttpStatusCode.BadRequest);
    public static readonly AppError StorageServiceError = new(1064, "There was an error with the storage service.", HttpStatusCode.InternalServerError);
    public static readonly AppError AvatarCouldNotBeUpdated = new(1065, "Avatar could not be updated.", HttpStatusCode.InternalServerError);
    public static readonly AppError UserIdentifierRequired = new(1066, "User identifier is required.", HttpStatusCode.BadRequest);
    public static readonly AppError InvalidPhoneNumber = new(1067, "The phone number is invalid.", HttpStatusCode.BadRequest);
    public static readonly AppError InvalidPostalCode = new(1068, "The postal code is invalid.", HttpStatusCode.BadRequest);
    public static readonly AppError InvalidAvatarUrl = new(1069, "The avatar URL is invalid.", HttpStatusCode.BadRequest);
    public static readonly AppError ProfileCouldNotBeUpdated = new(1070, "Profile could not be updated.", HttpStatusCode.InternalServerError);
    public static readonly AppError RequireSecurity = new(1071, "User does not have required security role or permissions.", HttpStatusCode.Forbidden);
    public static readonly AppError AuthProviderNotSupported = new(1072, "The specified auth provider is not supported.", HttpStatusCode.BadRequest);
    public static readonly AppError AuthorizationCodeRequired = new(1073, "The authorization code is required.", HttpStatusCode.BadRequest);
    public static readonly AppError AuthProviderUserIdentifierRequired = new(1074, "The user identifier from the auth provider is required.", HttpStatusCode.BadRequest);
    public static readonly AppError AuthProviderNotConfigured = new(1076, "The specified auth provider is not configured.", HttpStatusCode.BadRequest);
    public static readonly AppError OAuthCallbackNotSupported = new(1077, "The specified oAuth callback is not supported.", HttpStatusCode.BadRequest);
    public static readonly AppError InvalidAuthorizationCode = new(1078, "The authorization code is invalid.", HttpStatusCode.BadRequest);
    public static readonly AppError AuthProviderUnavailable = new(1079, "The auth provider is unavailable.", HttpStatusCode.ServiceUnavailable);
    public static readonly AppError AccessTokenExchangeFailed = new(1080, "The access token exchange failed.", HttpStatusCode.InternalServerError);
    public static readonly AppError InvalidAccessToken = new(1081, "The access token is invalid.", HttpStatusCode.BadRequest);
    public static readonly AppError OAuthProfileFetchFailed = new(1082, "Failed to fetch the OAuth profile.", HttpStatusCode.InternalServerError);
    public static readonly AppError InvalidOAuthProfile = new(1083, "The OAuth profile is invalid.", HttpStatusCode.BadRequest);
    public static readonly AppError NotImplemented = new(1075, "The method is not implemented.", HttpStatusCode.NotImplemented);
}
