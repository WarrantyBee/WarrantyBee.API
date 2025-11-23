package com.warrantybee.api.services.implementations;

import com.warrantybee.api.dto.internal.UserSearchFilter;
import com.warrantybee.api.dto.request.AvatarUpdateRequest;
import com.warrantybee.api.dto.request.ProfileUpdateRequest;
import com.warrantybee.api.dto.response.UserResponse;
import com.warrantybee.api.exceptions.*;
import com.warrantybee.api.helpers.Validator;
import com.warrantybee.api.repositories.interfaces.IUserRepository;
import com.warrantybee.api.services.interfaces.ICaptchaService;
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

    private final IStorageService _cloudinaryService;
    private final ICaptchaService _captchaService;
    private final IUserRepository _repository;

    @Autowired
    public UserService(IStorageService cloudinaryService, ICaptchaService captchaService, IUserRepository repository) {
        this._cloudinaryService = cloudinaryService;
        this._captchaService = captchaService;
        this._repository = repository;
    }

    @Override
    public void changeAvatar(AvatarUpdateRequest request) {
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
    }

    /** Validates the avatar update request. */
    private void _validate(AvatarUpdateRequest request) {
        if (request.getUserId() == null) {
            throw new UserIdentifierRequiredException();
        }
        if (Validator.isBlank(request.getCaptchaResponse())) {
            throw new CaptchaResponseRequiredException();
        }
        _validate(request.getAvatar());
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
