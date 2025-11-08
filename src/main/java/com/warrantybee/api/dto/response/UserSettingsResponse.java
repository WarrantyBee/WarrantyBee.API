package com.warrantybee.api.dto.response;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents the response containing user settings information.
 */
@Getter
@Setter
public class UserSettingsResponse {

    /** Indicates whether two-factor authentication (2FA) is enabled. */
    private Boolean is2FAEnabled;
}
