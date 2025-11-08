package com.warrantybee.api.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the detailed info of a user's login token.
 */
@Getter
@Setter
@AllArgsConstructor
public class LoginTokenDetails {

    /** Unique identifier of the user. */
    private Long userId;

    /** Email address of the user. */
    private String email;

    /** Generated login token for the user session. */
    private String token;
}
