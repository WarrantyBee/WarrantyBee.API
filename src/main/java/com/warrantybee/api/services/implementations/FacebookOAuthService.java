package com.warrantybee.api.services.implementations;

import com.restfb.AccessToken;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.User;
import com.warrantybee.api.configurations.FacebookConfiguration;
import com.warrantybee.api.dto.response.SocialUserProfileResponse;
import com.warrantybee.api.enumerations.AuthProvider;
import com.warrantybee.api.services.interfaces.IOAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * OAuth service implementation for Facebook authentication.
 * Handles exchanging the authorization code for an access token
 * and fetching the user's Facebook profile.
 */
@Service
public class FacebookOAuthService implements IOAuthService {

    /** Facebook configuration containing client, app credentials, and redirect URI. */
    @Autowired
    private FacebookConfiguration _facebookConfiguration;

    /**
     * Exchanges the Facebook authorization code for a user access token.
     *
     * @param authCode the authorization code returned by Facebook OAuth
     * @return the user access token
     */
    @Override
    public String getAccessToken(String authCode) {
        FacebookClient client = this._facebookConfiguration.getClient();
        final AccessToken accessToken = client.obtainUserAccessToken(
                _facebookConfiguration.getAppId(),
                _facebookConfiguration.getAppSecret(),
                _facebookConfiguration.getRedirectUri(),
                authCode
        );
        return accessToken.getAccessToken();
    }

    /**
     * Retrieves the user's Facebook profile using the provided access token.
     *
     * @param accessToken the Facebook user access token
     * @return the mapped user profile response
     */
    @Override
    public SocialUserProfileResponse getProfile(String accessToken) {
        FacebookClient client = _facebookConfiguration.getClient(accessToken);
        User user = client.fetchObject("me", User.class, Parameter.with("fields", "id,email,first_name,last_name"));
        return new SocialUserProfileResponse(
            user.getId(),
            AuthProvider.FACEBOOK.getName(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName()
        );
    }
}
