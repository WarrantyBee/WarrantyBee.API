package com.warrantybee.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** DTO for user response */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    /** User's identifier */
    private Long id;

    /** User's first name */
    private String firstname;

    /** User's last name */
    private String lastname;

    /** User's email */
    private String email;

    /** Additional user profile details */
    private UserProfileResponse details;

    public UserResponse(Long id, String firstname, String lastname, String email) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }
}
