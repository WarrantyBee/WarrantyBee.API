package com.warrantybee.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a request payload used to fetch a user's OAuth profile
 * after the OAuth authorization step.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OAuthProfileRequest extends BaseRequest {

    /**
     * The authorization code returned by the OAuth provider
     * after a successful authentication redirect.
     */
    private String code;

    /**
     * The identifier of the OAuth provider (e.g., 2 for "facebook", 3 for "google", 4 for "linkedin")
     * from which the authorization code was received.
     */
    private Byte authProvider;

    /**
     * Stores the OAuth callback type code indicating
     * the authentication flow (e.g., sign-in or sign-up).
     */
    private Byte callbackType;
}
