package com.warrantybee.api.repositories.interfaces;

import com.warrantybee.api.dto.internal.UserCreationRequest;
import com.warrantybee.api.dto.internal.UserSearchFilter;
import com.warrantybee.api.models.User;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for performing user-related database operations.
 */
@Repository
public interface IUserRepository {

    /**
     * Creates a new user record in the database.
     *
     * @param request The user details for account creation.
     * @return The generated user ID.
     */
    Long create(UserCreationRequest request);

    /**
     * Retrieves user details based on the provided search filter.
     *
     * @param filter The search criteria containing user ID or email.
     * @return The matching user, or {@code null} if not found.
     */
    User get(UserSearchFilter filter);
}
