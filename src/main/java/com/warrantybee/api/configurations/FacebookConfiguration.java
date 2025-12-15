package com.warrantybee.api.configurations;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import lombok.Getter;
import lombok.Setter;

/**
 * Configuration class for managing Facebook OAuth application settings and creating
 * {@link FacebookClient} instances using the RestFB library.
 */
@Getter
@Setter
public class FacebookConfiguration {

    /**
     * The Facebook application ID used for authentication and API calls.
     */
    private String appId;

    /**
     * The Facebook application secret key associated with the app.
     * This should be kept secure and never exposed to clients.
     */
    private String appSecret;

    /**
     * The redirect URI configured in the Facebook Developer Console.
     */
    private String redirectUri;

    /**
     * Creates a {@link FacebookClient} instance using the provided access token.
     *
     * @param accessToken the Facebook user access token obtained after authentication
     * @return a configured {@link FacebookClient} instance
     */
    public FacebookClient getClient(String accessToken) {
        return new DefaultFacebookClient(accessToken, Version.LATEST);
    }

    /**
     * Creates an unauthenticated {@link FacebookClient} instance.
     *
     * @return a {@link FacebookClient} instance using the latest Graph API version
     */
    public FacebookClient getClient() {
        return new DefaultFacebookClient(Version.LATEST);
    }
}
