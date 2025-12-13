package com.warrantybee.api.repositories.interfaces;

import com.warrantybee.api.dto.internal.LoginTokenDetails;
import com.warrantybee.api.dto.internal.PasswordResetRequest;
import com.warrantybee.api.dto.internal.UserSearchFilter;
import com.warrantybee.api.dto.request.ProfileUpdateRequest;
import com.warrantybee.api.dto.request.SignUpRequest;
import com.warrantybee.api.dto.response.UserResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Defines the data access layer (Repository) operations for managing {@code User} entities.
 * This interface decouples the service layer from the specific persistence implementation.
 */
@Repository
public interface IUserRepository {

    /**
     * Persists a new user record in the database.
     *
     * @param request A DTO containing all necessary data for user creation.
     * @return The unique system-generated ID of the newly created user.
     */
    Long create(SignUpRequest request);

    /**
     * Retrieves a user based on specific search criteria, such as ID or email.
     *
     * @param filter The search criteria encapsulated in a DTO.
     * @return A {@link UserResponse} DTO containing the user's details, or {@code null} if no user is found.
     */
    UserResponse get(UserSearchFilter filter);

    /**
     * Stores the user's login token details.
     *
     * @param details the login token details to store
     * @return true if the token is stored successfully, otherwise false
     */
    Boolean store(LoginTokenDetails details);

    /**
     * Validates the provided login token details.
     *
     * @param details the login token details to validate
     * @return true if the token is valid, otherwise false
     */
    Boolean validate(LoginTokenDetails details);

    /**
     * Resets the user's password based on the provided request.
     *
     * @param request contains user details and new password information
     * @return true if the password reset is successful, false otherwise
     */
    Boolean resetPassword(PasswordResetRequest request);

    /**
     * Retrieves all passwords (both old and currently active) for the specified user.
     *
     * @param id the identifier of the user
     * @return a list of passwords associated with the user
     */
    List<String> getPasswords(Long id);

    /**
     * Updates the user's profile information based on the provided request data.
     *
     * @param request the profile update details; must not be null
     * @return {@code true} if the profile was updated successfully; otherwise {@code false}
     */
    Boolean updateProfile(ProfileUpdateRequest request);
}
