package com.warrantybee.api.services.implementations;

import com.restfb.AccessToken;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.exception.FacebookNetworkException;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.User;
import com.warrantybee.api.configurations.FacebookConfiguration;
import com.warrantybee.api.dto.response.SocialUserProfileResponse;
import com.warrantybee.api.enumerations.AuthProvider;
import com.warrantybee.api.enumerations.OAuthCallback;
import com.warrantybee.api.exceptions.*;
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

    @Override
    public String getAccessToken(String authCode, OAuthCallback callback) {
        try {
            FacebookClient client = this._facebookConfiguration.getClient();
            final String queryParams = String.format("authProvider=facebook&action=%s", callback.getName());
            final String redirectUri = String.format("%s?%s", _facebookConfiguration.getRedirectUri(), queryParams);
            final AccessToken accessToken = client.obtainUserAccessToken(
                    _facebookConfiguration.getAppId(),
                    _facebookConfiguration.getAppSecret(),
                    redirectUri,
                    authCode
            );
            return accessToken.getAccessToken();
        }
        catch (FacebookOAuthException e) {
            throw new InvalidAuthorizationCodeException(null, e);
        }
        catch (FacebookNetworkException e) {
            throw new AuthProviderUnavailableException(null, e);
        }
        catch (Exception e) {
            throw new AccessTokenExchangeException(null, e);
        }
    }

    /**
     * Retrieves the user's Facebook profile using the provided access token.
     *
     * @param accessToken the Facebook user access token
     * @return the mapped user profile response
     */
    @Override
    public SocialUserProfileResponse getProfile(String accessToken) {
        try {
            FacebookClient client = _facebookConfiguration.getClient(accessToken);
            User user = client.fetchObject("me", User.class, Parameter.with("fields", "id,email,first_name,last_name"));
            return new SocialUserProfileResponse(
                    user.getId(),
                    AuthProvider.FACEBOOK.getCode(),
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName()
            );
        }
        catch (FacebookOAuthException e) {
            throw new InvalidAccessTokenException();
        }
        catch (FacebookNetworkException e) {
            throw new AuthProviderUnavailableException(null, e);
        }
        catch (Exception e) {
            throw new OAuthProfileFetchException(null, e);
        }
    }
}
