package com.warrantybee.api.services.interfaces;

import com.warrantybee.api.dto.response.SocialUserProfileResponse;
import com.warrantybee.api.enumerations.LoginProvider;
import com.warrantybee.api.exceptions.LoginProviderNotSupportedException;
import com.warrantybee.api.services.implementations.FacebookOAuthService;

import java.util.Objects;

/**
 * Defines the contract for OAuth-based authentication services.
 * <p>
 * Implementations of this interface handle the process of exchanging an
 * authorization code for an access token and retrieving the authenticated
 * user's profile information from a third-party OAuth provider such as
 * Facebook, Google, or LinkedIn.
 * </p>
 */
public interface IOAuthService {
    /**
     * Factory method for creating an {@link IOAuthService} instance based on the selected
     * {@link LoginProvider}.
     *
     * @param provider the OAuth login provider for which an implementation is requested
     * @return an instance of {@link IOAuthService} corresponding to the given provider
     * @throws LoginProviderNotSupportedException if the specified provider is not supported
     */
    static IOAuthService getInstance(LoginProvider provider) {
        IOAuthService instance = null;

        if (provider == LoginProvider.FACEBOOK) {
            instance = new FacebookOAuthService();
        } else {
            throw new LoginProviderNotSupportedException();
        }

        return instance;
    }

    /**
     * Exchanges an OAuth authorization code for an access token.
     * <p>
     * This method is typically called after the user is redirected back to
     * the application from the OAuth provider during the login/signup flow.
     * </p>
     *
     * @param authCode the authorization code received from the OAuth provider
     * @return the access token that can be used for subsequent API calls
     */
    String getAccessToken(String authCode);

    /**
     * Retrieves the authenticated user's profile information using the access token.
     * <p>
     * This may include fields such as name, email, profile image, gender,
     * and other data supported by the specific OAuth provider.
     * </p>
     *
     * @param accessToken the valid access token issued by the OAuth provider
     * @return a {@link SocialUserProfileResponse} containing user details fetched from the provider
     */
    SocialUserProfileResponse getProfile(String accessToken);
}
