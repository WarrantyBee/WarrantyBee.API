package com.warrantybee.api.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a request to reset a user's password.
 */
@Getter
@Setter
@AllArgsConstructor
public class PasswordResetRequest {
    /** The ID of the user whose password is to be reset. */
    private Long userId;

    /** The new password to set for the user. */
    private String newPassword;
}
