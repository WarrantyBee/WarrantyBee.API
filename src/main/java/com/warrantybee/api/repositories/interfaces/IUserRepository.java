package com.warrantybee.api.repositories.interfaces;

import com.warrantybee.api.dto.internal.UserCreationRequest;
import com.warrantybee.api.dto.internal.UserSearchFilter;
import com.warrantybee.api.dto.response.UserResponse;
import org.springframework.stereotype.Repository;

/**
 * Defines data access operations for users.
 */
@Repository
public interface IUserRepository {

    /**
     * Registers a new user.
     *
     * @param request User creation details.
     * @return Generated user ID.
     */
    Long create(UserCreationRequest request);

    /**
     * Fetches a user by ID or email.
     *
     * @param filter Search criteria.
     * @return User details, or {@code null} if not found.
     */
    UserResponse get(UserSearchFilter filter);
}
