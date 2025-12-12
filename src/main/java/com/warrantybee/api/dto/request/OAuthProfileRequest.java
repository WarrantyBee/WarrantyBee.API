package com.warrantybee.api.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a request payload used to fetch a user's OAuth profile
 * after the OAuth authorization step.
 */
@Getter
@Setter
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
    private Integer provider;
}
