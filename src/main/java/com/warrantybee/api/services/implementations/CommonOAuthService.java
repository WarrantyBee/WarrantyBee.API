package com.warrantybee.api.services.implementations;

import com.warrantybee.api.dto.request.OAuthProfileRequest;
import com.warrantybee.api.dto.response.SocialUserProfileResponse;
import com.warrantybee.api.enumerations.AuthProvider;
import com.warrantybee.api.enumerations.OAuthCallback;
import com.warrantybee.api.exceptions.*;
import com.warrantybee.api.helpers.Validator;
import com.warrantybee.api.services.interfaces.ICaptchaService;
import com.warrantybee.api.services.interfaces.IOAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Provides common OAuth validation and dispatching logic
 * shared across all OAuth provider implementations.
 */
@Service
public class CommonOAuthService implements IOAuthService {

    private final ICaptchaService _captchaService;
    private final Map<String, IOAuthService> _oAuthServices;

    @Autowired
    public CommonOAuthService(ICaptchaService captchaService, Map<String, IOAuthService> oAuthServices) {
        this._captchaService = captchaService;
        this._oAuthServices = oAuthServices;
    }

    /**
     * Validates the request, checks captcha, resolves the appropriate OAuth service,
     * exchanges the authorization code for an access token, and retrieves the user profile.
     *
     * @param request the OAuth profile request containing provider, auth code and callback type.
     * @return the user's social profile information
     */
    public SocialUserProfileResponse getProfile(OAuthProfileRequest request) {
        _validate(request);
        boolean hasValidCaptcha = _captchaService.validate(request.getCaptchaResponse());

        if (hasValidCaptcha) {
            AuthProvider provider = AuthProvider.getValue(request.getAuthProvider());
            String serviceName = provider.getName().toLowerCase() + "OAuthService";
            IOAuthService oAuthService = _oAuthServices.get(serviceName);

            if (oAuthService == null) {
                throw new AuthProviderNotSupportedException();
            }

            OAuthCallback callback = OAuthCallback.getValue(request.getCallbackType());
            final String accessToken = oAuthService.getAccessToken(request.getCode(), callback);
            return oAuthService.getProfile(accessToken);
        }
        else {
            throw new CaptchaVerificationFailedException();
        }
    }

    @Override
    public String getAccessToken(String authCode, OAuthCallback callback) {
        throw new NotImplementedException();
    }

    @Override
    public SocialUserProfileResponse getProfile(String accessToken) {
        throw new NotImplementedException();
    }

    /**
     * Validates the incoming OAuth profile request.
     * Ensures request body, authorization code, provider, and callback type are valid.
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

        AuthProvider provider = AuthProvider.getValue(request.getAuthProvider());
        if (provider == AuthProvider.NONE || provider == AuthProvider.INTERNAL) {
            throw new AuthProviderNotSupportedException();
        }

        OAuthCallback callback = OAuthCallback.getValue(request.getCallbackType());
        if (callback == OAuthCallback.NONE) {
            throw new OAuthCallbackNotSupportedException();
        }
    }
}
