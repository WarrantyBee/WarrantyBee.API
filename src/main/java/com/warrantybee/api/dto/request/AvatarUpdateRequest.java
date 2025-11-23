package com.warrantybee.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * Represents the request payload for updating a user's profile avatar.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AvatarUpdateRequest extends BaseRequest {

    /**
     * The unique identifier of the user whose avatar is being updated.
     */
    private Long userId;

    /**
     * The avatar image file uploaded by the user.
     */
    private MultipartFile avatar;
}
