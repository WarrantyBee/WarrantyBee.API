package com.warrantybee.api.repositories.interfaces;

import com.warrantybee.api.dto.internal.LoginTokenDetails;
import com.warrantybee.api.dto.internal.PasswordResetRequest;
import com.warrantybee.api.dto.internal.UserCreationRequest;
import com.warrantybee.api.dto.internal.UserSearchFilter;
import com.warrantybee.api.dto.response.UserResponse;
import org.springframework.stereotype.Repository;

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
    Long create(UserCreationRequest request);

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
}
