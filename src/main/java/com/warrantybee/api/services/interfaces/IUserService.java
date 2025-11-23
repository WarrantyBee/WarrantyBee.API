package com.warrantybee.api.services.interfaces;

import com.warrantybee.api.dto.request.AvatarUpdateRequest;

/**
 * Defines user-related operations within the application.
 */
public interface IUserService {

    /**
     * Updates the user's profile picture using the provided avatar update request.
     *
     * @param request the avatar update details
     */
    void changeAvatar(AvatarUpdateRequest request);
}
