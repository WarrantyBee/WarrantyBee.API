package com.warrantybee.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents the normalized user profile information obtained from an OAuth provider.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SocialUserProfileResponse {

    /**
     * The unique identifier of the user as provided by the OAuth provider.
     */
    private String id;

    /**
     * The code of the OAuth provider (e.g., 2 for "facebook", 3 for "google", 4 for "linkedin")
     * from which this profile data was retrieved.
     */
    private int provider;

    /**
     * The email address associated with the user's social account.
     * May be {@code null} if the provider does not return or permit access to email.
     */
    private String email;

    /**
     * The user's first name as returned by the provider.
     */
    private String firstname;

    /**
     * The user's last name as returned by the provider.
     */
    private String lastname;
}
