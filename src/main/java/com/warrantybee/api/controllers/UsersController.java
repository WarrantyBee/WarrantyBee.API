package com.warrantybee.api.controllers;

import com.warrantybee.api.dto.request.AvatarUpdateRequest;
import com.warrantybee.api.dto.response.APIResponse;
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
     * Updates the avatar (profile picture) of the specified user.
     *
     * @param userId the ID of the user whose avatar is being updated
     * @param avatar the uploaded image file provided as multipart/form-data
     * @param request the request body containing the captcha response
     * @return a success {@link APIResponse}, wrapped in a {@link ResponseEntity}
     */
@PostMapping(value = "/{userId}/changeavatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<APIResponse<?>> changeAvatar(
            @PathVariable Long userId,
            @RequestPart("avatar") MultipartFile avatar,
            @RequestBody AvatarUpdateRequest request) {
        request.setUserId(userId);
        request.setAvatar(avatar);
        _service.changeAvatar(request);
        return ResponseEntity.ok(new APIResponse<>(null, null));
    }
}
