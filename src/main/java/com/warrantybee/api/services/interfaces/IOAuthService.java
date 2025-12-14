package com.warrantybee.api.services.interfaces;

import com.warrantybee.api.dto.response.SocialUserProfileResponse;
import com.warrantybee.api.enumerations.AuthProvider;
import com.warrantybee.api.enumerations.OAuthCallback;
import com.warrantybee.api.exceptions.AuthProviderNotSupportedException;
import com.warrantybee.api.services.implementations.FacebookOAuthService;

/**
 * Defines the contract for OAuth-based authentication services.
 */
public interface IOAuthService {
    /**
     * Exchanges an OAuth authorization code for an access token.
     *
     * @param authCode the authorization code received from the OAuth provider
     * @param callback the OAuth callback action determining the authentication flow
     * @return the access token that can be used for subsequent API calls
     */
    String getAccessToken(String authCode, OAuthCallback callback);

    /**
     * Retrieves the authenticated user's profile information from the OAuth provider
     * using the supplied access token.
     *
     * @param accessToken the access token issued by the OAuth provider, used to authorize
     *                    the profile information request
     * @return a {@link SocialUserProfileResponse} containing the user's profile details
     *         fetched from the provider
     */
    SocialUserProfileResponse getProfile(String accessToken);
}
