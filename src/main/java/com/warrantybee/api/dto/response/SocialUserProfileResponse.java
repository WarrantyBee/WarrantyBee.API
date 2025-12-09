package com.warrantybee.api.dto.response;

import com.warrantybee.api.enumerations.LoginProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SocialUserProfileResponse {
    private String id;
    private Byte provider;
    private String email;
    private String firstname;
    private String lastname;
}
