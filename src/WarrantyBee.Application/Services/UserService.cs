using WarrantyBee.Application.Abstractions.Persistence;
using WarrantyBee.Application.Abstractions.Services;
using WarrantyBee.Application.Common;
using WarrantyBee.Application.Contracts.Users;
using WarrantyBee.Domain.Enums;
using WarrantyBee.Domain.Exceptions;

namespace WarrantyBee.Application.Services;

/// <summary>
/// Service for managing user-related operations such as profile retrieval and updates.
/// </summary>
public class UserService : IUserService
{
    private readonly ICurrentUserContext _userContext;
    private readonly IStorageService _storageService;
    private readonly ICaptchaService _captchaService;
    private readonly IUserRepository _userRepository;

    /// <summary>
    /// Initializes a new instance of the <see cref="UserService"/> class.
    /// </summary>
    /// <param name="userContext">The context for the currently authenticated user.</param>
    /// <param name="storageService">Service for file storage operations.</param>
    /// <param name="captchaService">Service for captcha validation.</param>
    /// <param name="userRepository">Repository for user data.</param>
    public UserService(
        ICurrentUserContext userContext,
        IStorageService storageService,
        ICaptchaService captchaService,
        IUserRepository userRepository)
    {
        _userContext = userContext;
        _storageService = storageService;
        _captchaService = captchaService;
        _userRepository = userRepository;
    }

    /// <summary>
    /// Retrieves the profile of the current user.
    /// </summary>
    /// <returns>The <see cref="UserResponse"/> containing user profile information, or null if not found.</returns>
    /// <exception cref="ApiException">Thrown if the user is unauthenticated or not found.</exception>
    public async Task<UserResponse?> GetAsync()
    {
        var userId = _userContext.UserId;
        if (!userId.HasValue) throw new ApiException(Errors.Unauthenticated);

        var user = await _userRepository.GetAsync(new UserSearchFilter(userId.Value, null));
        if (user == null) throw new ApiException(Errors.UserNotFound);

        return user;
    }

    /// <summary>
    /// Changes the user's avatar image.
    /// </summary>
    /// <param name="userId">The ID of the user. If 0, uses the current user's ID.</param>
    /// <param name="avatarStream">The stream containing the avatar image data.</param>
    /// <param name="fileName">The name of the file.</param>
    /// <param name="contentType">The content type of the file.</param>
    /// <returns>An <see cref="AvatarResponse"/> containing the new avatar URL.</returns>
    /// <exception cref="ApiException">Thrown if the file is invalid, user is not found, or update fails.</exception>
    public async Task<AvatarResponse> ChangeAvatarAsync(long userId, Stream avatarStream, string fileName, string contentType)
    {
        // Validation logic ported from Java
        if (avatarStream.Length == 0) throw new ApiException(Errors.FileIsEmpty);
        if (avatarStream.Length > 2 * 1024 * 1024) throw new ApiException(Errors.FileExceededAllowedSize);
        // ... (Image format validation would go here)

        var currentUserId = userId == 0 ? _userContext.UserId : userId;
        if (!currentUserId.HasValue) throw new ApiException(Errors.UserIdentifierRequired);

        var user = await _userRepository.GetAsync(new UserSearchFilter(currentUserId.Value, null));
        if (user == null) throw new ApiException(Errors.UserNotFound);

        // Delete old avatar
        var previousAvatarUrl = user.Profile?.AvatarUrl;
        if (!string.IsNullOrWhiteSpace(previousAvatarUrl))
        {
            await _storageService.DeleteByUrlAsync(previousAvatarUrl);
        }

        // Upload new avatar
        var newUrl = await _storageService.UploadAsync(avatarStream, fileName, contentType);
        if (string.IsNullOrWhiteSpace(newUrl)) throw new ApiException(Errors.StorageServiceError);

        // Update profile
        var success = await _userRepository.UpdateProfileAsync(new ProfileUpdateRequest
        {
            UserId = currentUserId.Value,
            AvatarUrl = newUrl
        });

        if (!success)
        {
            await _storageService.DeleteByUrlAsync(newUrl);
            throw new ApiException(Errors.AvatarCouldNotBeUpdated);
        }

        return new AvatarResponse(newUrl);
    }

    /// <summary>
    /// Updates the current user's profile information.
    /// </summary>
    /// <param name="request">The profile update request containing the new information.</param>
    /// <returns>A <see cref="Task"/> representing the asynchronous operation.</returns>
    /// <exception cref="ApiException">Thrown if the user is unauthenticated or validation fails.</exception>
    public async Task UpdateProfileAsync(ProfileUpdateRequest request)
    {
        var currentUserId = _userContext.UserId;
        if (!currentUserId.HasValue) throw new ApiException(Errors.Unauthenticated);

        request.UserId = currentUserId.Value;
        ValidateProfileUpdate(request);

        var success = await _userRepository.UpdateProfileAsync(request);
        if (!success) throw new ApiException(Errors.ProfileCouldNotBeUpdated);
    }

    private void ValidateProfileUpdate(ProfileUpdateRequest request)
    {
        if (request.AddressLine1 != null && Validator.IsBlank(request.AddressLine1)) throw new ApiException(Errors.AddressRequired);
        if (request.City != null && Validator.IsBlank(request.City)) throw new ApiException(Errors.CityRequired);
        if (request.PostalCode != null && !Validator.IsPostalCode(request.PostalCode)) throw new ApiException(Errors.InvalidPostalCode);
        if (request.PhoneCode != null && !Validator.IsPhoneCode(request.PhoneCode)) throw new ApiException(Errors.InvalidPhoneCode);
        if (request.PhoneNumber != null && !Validator.IsPhoneNumber(request.PhoneNumber)) throw new ApiException(Errors.InvalidPhoneNumber);
        if (request.AvatarUrl != null && !Validator.IsUrl(request.AvatarUrl)) throw new ApiException(Errors.InvalidAvatarUrl);
    }
}
