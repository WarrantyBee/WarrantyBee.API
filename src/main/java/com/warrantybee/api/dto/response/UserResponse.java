package com.warrantybee.api.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents user information returned in API responses.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    /** Unique identifier of the user. */
    private Long id;

    /** First name of the user. */
    private String firstname;

    /** Last name of the user. */
    private String lastname;

    /** Email address of the user. */
    private String email;

    /** Password hash of the user. */
    @JsonIgnore
    private String password;

    /** Extended profile details of the user. */
    private UserProfileResponse details;
}
