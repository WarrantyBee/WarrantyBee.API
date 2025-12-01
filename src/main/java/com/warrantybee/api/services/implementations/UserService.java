package com.warrantybee.api.services.implementations;

import com.warrantybee.api.dto.internal.UserSearchFilter;
import com.warrantybee.api.dto.request.AvatarUpdateRequest;
import com.warrantybee.api.dto.request.ProfileUpdateRequest;
import com.warrantybee.api.dto.response.AvatarResponse;
import com.warrantybee.api.dto.response.UserResponse;
import com.warrantybee.api.exceptions.*;
import com.warrantybee.api.helpers.Validator;
import com.warrantybee.api.repositories.interfaces.IUserRepository;
import com.warrantybee.api.services.interfaces.ICaptchaService;
import com.warrantybee.api.services.interfaces.IHttpContext;
import com.warrantybee.api.services.interfaces.IStorageService;
import com.warrantybee.api.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service implementation for handling user-related operations such as
 * updating profile information, settings and changing profile avatars.
 */
@Service
public class UserService implements IUserService {

    private final IHttpContext _httpContext;
    private final IStorageService _cloudinaryService;
    private final ICaptchaService _captchaService;
    private final IUserRepository _repository;

    /**
     * Constructs a new {@code UserService} with the required dependencies.
     *
     * @param httpContext       the HTTP context service used to access request-scoped user information
     * @param cloudinaryService the storage service responsible for handling avatar uploads and related operations
     * @param captchaService    the service used for captcha verification
     * @param repository        the user repository for performing database operations
     */
    @Autowired
    public UserService(IHttpContext httpContext, IStorageService cloudinaryService, ICaptchaService captchaService, IUserRepository repository) {
        this._httpContext = httpContext;
        this._cloudinaryService = cloudinaryService;
        this._captchaService = captchaService;
        this._repository = repository;
    }

    @Override
    public UserResponse get() {
        UserSearchFilter filter = new UserSearchFilter(_httpContext.getUserId(), null);
        UserResponse user = _repository.get(filter);

        if (user == null) {
            throw new UserNotRegisteredException();
        }

        return user;
    }

    @Override
    public AvatarResponse changeAvatar(AvatarUpdateRequest request) {
        if (request != null) {
            request.setUserId(_httpContext.getUserId());
        }

        _validate(request);
        boolean isValid = _captchaService.validate(request.getCaptchaResponse());
        if (!isValid) {
            throw new CaptchaVerificationFailedException();
        }

        UserSearchFilter filter = new UserSearchFilter(request.getUserId(), null);
        UserResponse user = _repository.get(filter);
        if (user == null) {
            throw new UserNotRegisteredException();
        }

        String previousAvatarUrl = CloudinaryStorageService.getPublicId(user.getProfile().getAvatarUrl());
        if (!Validator.isBlank(previousAvatarUrl)) {
            _cloudinaryService.delete(previousAvatarUrl);
        }

        String currentAvatarUrl = _cloudinaryService.upload(request.getAvatar());
        if (Validator.isBlank(currentAvatarUrl)) {
            throw new StorageServiceException("Something went wrong while uploading file to the storage.");
        }
        else {
            ProfileUpdateRequest profileUpdateRequest = new ProfileUpdateRequest();
            profileUpdateRequest.setUserId(request.getUserId());
            profileUpdateRequest.setAvatarUrl(currentAvatarUrl);
            Boolean isSuccess = _repository.updateProfile(profileUpdateRequest);
            if (!isSuccess) {
                String publicId = CloudinaryStorageService.getPublicId(currentAvatarUrl);
                try {
                    _cloudinaryService.delete(publicId);
                } catch (Exception ignored) {}
                throw new AvatarCouldNotBeUpdatedException();
            }
        }

        return new AvatarResponse(currentAvatarUrl);
    }

    @Override
    public void updateProfile(ProfileUpdateRequest request) {
        if (request != null) {
            request.setUserId(_httpContext.getUserId());
        }

        _validate(request);

        if (_captchaService.validate(request.getCaptchaResponse())) {
            Boolean updated = _repository.updateProfile(request);
            if (!updated) {
                throw new ProfileCouldNotBeUpdatedException();
            }
        }
        else {
            throw new CaptchaVerificationFailedException();
        }
    }

    /** Validates the avatar update request. */
    private void _validate(AvatarUpdateRequest request) {
        if (request == null) {
            throw new RequestBodyEmptyException();
        }
        if (request.getUserId() == null) {
            throw new UserIdentifierRequiredException();
        }
        _validate(request.getAvatar());
    }

    /** Validates the profile update request. */
    private void _validate(ProfileUpdateRequest request) {
        if (request == null) {
            throw new RequestBodyEmptyException();
        }
        if (request.getUserId() == null) {
            throw new UserIdentifierRequiredException();
        }
        if (request.getAddressLine1() != null && Validator.isBlank(request.getAddressLine1())) {
            throw new AddressRequiredException("Proper value of Address Line 1 should be given.");
        }
        if (request.getAddressLine2() != null && Validator.isBlank(request.getAddressLine2())) {
            throw new AddressRequiredException("Proper value of Address Line 2 should be given.");
        }
        if (request.getCity() != null && Validator.isBlank(request.getCity())) {
            throw new CityRequiredException("Proper value of City should be given");
        }
        if (request.getPostalCode() != null && !Validator.isPostalCode(request.getPostalCode())) {
            throw new InvalidPostalCodeException();
        }
        if (request.getPhoneCode() != null && !Validator.isPhoneCode(request.getPhoneCode())) {
            throw new InvalidPhoneCodeException();
        }
        if (request.getPhoneNumber() != null && !Validator.isPhoneNumber(request.getPhoneNumber())) {
            throw new InvalidPhoneNumberException();
        }
        if (request.getAvatarUrl() != null && !Validator.isUrl(request.getAvatarUrl())) {
            throw new InvalidAvatarUrlException();
        }
    }

    /** Validates the file for updating avatar. */
    private void _validate(MultipartFile file) {
        if (Validator.isEmpty(file)) {
            throw new FileIsEmptyException();
        }
        if (!Validator.isImage(file)) {
            throw new InvalidFileFormatException("Only valid image formats are allowed for the profile picture.");
        }
        if (!Validator.hasSize(file, 2 * 1024 * 1024)) {
            throw new FileExceededAllowedSizeException("Profile picture size must be within 2 MB.");
        }
    }
}
