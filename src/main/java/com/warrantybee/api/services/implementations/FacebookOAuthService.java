package com.warrantybee.api.services.implementations;

import com.restfb.AccessToken;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.User;
import com.warrantybee.api.configurations.FacebookConfiguration;
import com.warrantybee.api.dto.response.SocialUserProfileResponse;
import com.warrantybee.api.enumerations.LoginProvider;
import com.warrantybee.api.services.interfaces.IOAuthService;
import org.springframework.beans.factory.annotation.Autowired;

public class FacebookOAuthService implements IOAuthService {

    @Autowired
    private FacebookConfiguration _facebookConfiguration;

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

    @Override
    public SocialUserProfileResponse getProfile(String accessToken) {
        FacebookClient client = _facebookConfiguration.getClient(accessToken);
        User user = client.fetchObject("me", User.class,
            Parameter.with(
                "fields",
                "id,first_name,last_name,email"
            ));

        SocialUserProfileResponse userProfile = new SocialUserProfileResponse();
        userProfile.setProvider(LoginProvider.FACEBOOK.getCode());
        userProfile.setId(user.getId());
        userProfile.setEmail(user.getEmail());
        userProfile.setFirstname(user.getFirstName());
        userProfile.setLastname(user.getLastName());

        return userProfile;
    }
}
