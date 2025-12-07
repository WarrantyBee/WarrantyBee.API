package com.warrantybee.api.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.warrantybee.api.dto.internal.UserAuthorization;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) containing core user account information.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    /**
     * The unique database identifier of the user.
     */
    private Long id;

    /**
     * The first name (given name) of the user.
     */
    private String firstname;

    /**
     * The last name (surname) of the user.
     */
    private String lastname;

    /**
     * The primary email address of the user.
     */
    private String email;

    /**
     * The hashed password of the user. This field is ignored during JSON serialization
     * due to the {@code @JsonIgnore} annotation for security reasons.
     */
    @JsonIgnore
    private String password;

    /**
     * Extended profile details, including geographical and preference information,
     * provided as a nested {@link UserProfileResponse} DTO.
     */
    private UserProfileResponse profile;

    /**
     * Authorization details (role and permissions) for the user.
     * Ignored during JSON serialization.
     */
    @JsonIgnore
    private UserAuthorization authorizationContext;
}
