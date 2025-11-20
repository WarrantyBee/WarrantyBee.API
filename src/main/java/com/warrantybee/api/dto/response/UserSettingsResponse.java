package com.warrantybee.api.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * Represents the response containing user settings information.
 */
@Getter
@Setter
public class UserSettingsResponse {

    /** Indicates whether two-factor authentication (2FA) is enabled. */
    private Boolean is2FAEnabled;

    /** The timestamp indicating when the user's password was last updated. */
    private Timestamp passwordUpdatedAt;
}
