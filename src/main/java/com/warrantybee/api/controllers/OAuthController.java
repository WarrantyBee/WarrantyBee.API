package com.warrantybee.api.controllers;

import com.warrantybee.api.dto.request.OAuthProfileRequest;
import com.warrantybee.api.dto.response.APIResponse;
import com.warrantybee.api.dto.response.SocialUserProfileResponse;
import com.warrantybee.api.services.implementations.CommonOAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller responsible for handling OAuth-related operations such as
 * retrieving user profile information from supported social authentication providers.
 */
@RestController
@RequestMapping("/api/oauth")
public class OAuthController {

    @Autowired
    private CommonOAuthService _commonOAuthService;

    /**
     * Retrieves a normalized social user profile from an OAuth provider using the
     * supplied authentication details.
     *
     * @param request the request payload containing provider information and access token/auth code
     * @return a {@link ResponseEntity} containing the user's social profile on success
     */
    @PostMapping("/profile")
    public ResponseEntity<APIResponse<SocialUserProfileResponse>> get(@RequestBody OAuthProfileRequest request) {
        SocialUserProfileResponse userProfile = _commonOAuthService.getProfile(request);
        return ResponseEntity.ok(new APIResponse<>(userProfile, null));
    }
}
