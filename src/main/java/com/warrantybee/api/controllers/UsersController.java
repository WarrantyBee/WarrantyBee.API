package com.warrantybee.api.controllers;

import com.warrantybee.api.annotations.RequireSecurity;
import com.warrantybee.api.annotations.RolePermission;
import com.warrantybee.api.dto.request.AvatarUpdateRequest;
import com.warrantybee.api.dto.request.ProfileUpdateRequest;
import com.warrantybee.api.dto.response.APIResponse;
import com.warrantybee.api.dto.response.AvatarResponse;
import com.warrantybee.api.dto.response.UserResponse;
import com.warrantybee.api.enumerations.SecurityPermission;
import com.warrantybee.api.enumerations.SecurityRole;
import com.warrantybee.api.services.interfaces.IUserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Handles user-related API operations such as profile updates and avatar changes.
 */
@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final IUserService _service;

    /**
     * Constructs a new {@code UsersController} with the required user service.
     *
     * @param service the user service implementation
     */
    public UsersController(IUserService service) {
        this._service = service;
    }

    /**
     * Retrieves a user profile from the context.
     *
     * @return a {@link ResponseEntity} containing an {@link APIResponse} with the user details
     */
    @RequireSecurity({
        @RolePermission(role = SecurityRole.CUSTOMER, permissions = { SecurityPermission.ACCESS_PROFILE })
    })
    @GetMapping("/profile")
    public ResponseEntity<APIResponse<UserResponse>> get() {
        UserResponse user = _service.get();
        return ResponseEntity.ok(new APIResponse<>(user, null));
    }

    /**
     * Updates the avatar (profile picture) of the specified user.
     *
     * @param avatar the uploaded image file provided as multipart/form-data
     * @param captchaResponse the captcha response
     * @return a success {@link APIResponse} with the new avatar url, wrapped in a {@link ResponseEntity}
     */
    @RequireSecurity({
        @RolePermission(role = SecurityRole.CUSTOMER, permissions = { SecurityPermission.CHANGE_AVATAR })
    })
    @PostMapping(value = "/profile/changeavatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<APIResponse<AvatarResponse>> changeAvatar(
            @RequestPart("avatar") MultipartFile avatar,
            @RequestPart ("captchaResponse") String captchaResponse) {
        AvatarUpdateRequest request = new AvatarUpdateRequest();
        request.setAvatar(avatar);
        request.setCaptchaResponse(captchaResponse);
        AvatarResponse avatarResponse = _service.changeAvatar(request);
        return ResponseEntity.ok(new APIResponse<>(avatarResponse, null));
    }

    /**
     * Updates the profile details of the currently authenticated user.
     *
     * @param request the profile update request containing fields to update
     * @return {@link ResponseEntity} containing a generic {@link APIResponse} with no payload
     * @apiNote This is a PATCH operation, meaning only partial fields are expected and updated.
     */
    @RequireSecurity({
        @RolePermission(role = SecurityRole.CUSTOMER, permissions = { SecurityPermission.EDIT_PROFILE })
    })
    @PatchMapping("/profile")
    public ResponseEntity<APIResponse<?>> updateProfile(@RequestBody ProfileUpdateRequest request) {
        _service.updateProfile(request);
        return ResponseEntity.ok(new APIResponse<>(null, null));
    }
}
