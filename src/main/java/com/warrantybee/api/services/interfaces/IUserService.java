package com.warrantybee.api.services.interfaces;

import com.warrantybee.api.dto.request.AvatarUpdateRequest;
import com.warrantybee.api.dto.response.AvatarResponse;
import com.warrantybee.api.dto.response.UserResponse;

/**
 * Defines user-related operations within the application.
 */
public interface IUserService {

    /**
     * Retrieves a user details from the context.
     *
     * @return a {@link UserResponse} containing the user's details
     */
    UserResponse get();

    /**
     * Updates the user's profile picture using the provided avatar update request.
     *
     * @param request the avatar update details
     * @returns the new avatar details
     */
    AvatarResponse changeAvatar(AvatarUpdateRequest request);
}
