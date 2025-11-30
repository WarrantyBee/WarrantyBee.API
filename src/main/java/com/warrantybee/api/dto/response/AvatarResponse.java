package com.warrantybee.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO representing the avatar details of a user.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AvatarResponse {

    /**
     * The URL of the user's avatar image.
     */
    private String url;
}
