package com.warrantybee.api.dto.response;

import com.warrantybee.api.enumerations.LoginProvider;

public class SocialUserProfileResponse {
    private String id;
    private LoginProvider provider;
    private String email;
    private String firstname;
    private String lastname;
}
