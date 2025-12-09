package com.warrantybee.api.services.interfaces;

import com.warrantybee.api.dto.response.SocialUserProfileResponse;
import com.warrantybee.api.enumerations.AuthProvider;
import com.warrantybee.api.exceptions.AuthProviderNotSupportedException;
import com.warrantybee.api.services.implementations.FacebookOAuthService;

/**
 * Defines the contract for OAuth-based authentication services.
 */
public interface IOAuthService {
    /**
     * Factory method for creating an {@link IOAuthService} instance based on the selected
     * {@link AuthProvider}.
     *
     * @param provider the OAuth login provider for which an implementation is requested
     * @return an instance of {@link IOAuthService} corresponding to the given provider
     */
    static IOAuthService getInstance(AuthProvider provider) {
        IOAuthService instance = null;

        if (provider == AuthProvider.FACEBOOK) {
            instance = new FacebookOAuthService();
        } else {
            throw new AuthProviderNotSupportedException();
        }

        return instance;
    }

    /**
     * Convenience factory method that resolves an {@link AuthProvider} from its string
     * representation and returns the corresponding {@link IOAuthService} instance.
     *
     * @param provider the name of the authentication provider (e.g., "facebook", "google")
     * @return an {@link IOAuthService} instance for the resolved provider
     */
    static IOAuthService getInstance(String provider) {
        AuthProvider authProvider = AuthProvider.getValue(provider);
        return getInstance(authProvider);
    }

    /**
     * Exchanges an OAuth authorization code for an access token.
     *
     * @param authCode the authorization code received from the OAuth provider
     * @return the access token that can be used for subsequent API calls
     */
    String getAccessToken(String authCode);

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
