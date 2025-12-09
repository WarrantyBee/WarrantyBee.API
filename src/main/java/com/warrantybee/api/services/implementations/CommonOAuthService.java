package com.warrantybee.api.services.implementations;

import com.warrantybee.api.dto.request.OAuthProfileRequest;
import com.warrantybee.api.dto.response.SocialUserProfileResponse;
import com.warrantybee.api.enumerations.AuthProvider;
import com.warrantybee.api.exceptions.AuthorizationCodeRequired;
import com.warrantybee.api.exceptions.AuthProviderNotSupportedException;
import com.warrantybee.api.exceptions.CaptchaVerificationFailedException;
import com.warrantybee.api.exceptions.RequestBodyEmptyException;
import com.warrantybee.api.helpers.Validator;
import com.warrantybee.api.services.interfaces.ICaptchaService;
import com.warrantybee.api.services.interfaces.IOAuthService;
import com.warrantybee.api.exceptions.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Provides common OAuth validation and dispatching logic
 * shared across all OAuth provider implementations.
 */
@Service
public class CommonOAuthService implements IOAuthService {

    @Autowired
    private ICaptchaService _captchaService;

    /**
     * Validates the request, checks captcha, resolves the appropriate OAuth service,
     * exchanges the authorization code for an access token, and retrieves the user profile.
     *
     * @param request the OAuth profile request containing provider and auth code
     * @return the user's social profile information
     */
    public SocialUserProfileResponse getProfile(OAuthProfileRequest request) {
        _validate(request);
        boolean hasValidCaptcha = _captchaService.validate(request.getCaptchaResponse());

        if (hasValidCaptcha) {
            IOAuthService oAuthService = IOAuthService.getInstance(request.getProvider());
            final String accessToken = oAuthService.getAccessToken(request.getCode());
            return oAuthService.getProfile(accessToken);
        }
        else {
            throw new CaptchaVerificationFailedException();
        }
    }

    @Override
    public String getAccessToken(String authCode) {
        throw new NotImplementedException();
    }

    @Override
    public SocialUserProfileResponse getProfile(String accessToken) {
        throw new NotImplementedException();
    }

    /**
     * Validates the incoming OAuth profile request.
     * Ensures request body, authorization code, and provider are valid.
     *
     * @param request the incoming OAuth profile request
     */
    private void _validate(OAuthProfileRequest request) {
        if (request == null) {
            throw new RequestBodyEmptyException();
        }
        if (Validator.isBlank(request.getCode())) {
            throw new AuthorizationCodeRequired();
        }
        if (AuthProvider.getValue(request.getProvider()) == AuthProvider.NONE) {
            throw new AuthProviderNotSupportedException();
        }
    }
}
