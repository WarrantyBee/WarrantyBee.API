package com.warrantybee.api.configurations;

import lombok.Getter;
import lombok.Setter;

/**
 * Configuration class that defines user profile-related settings.
 */
@Getter
@Setter
public class ProfileConfiguration {

    /**
     * The duration (in minutes) defining how soon a password can be reset again
     * after a previous reset.
     */
    private Long passwordResetWindow;
}
