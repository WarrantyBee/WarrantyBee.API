package com.warrantybee.api.repositories.interfaces;

import com.warrantybee.api.models.User;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for User entity.
 * Extends IBaseRepository to inherit standard CRUD operations.
 */
@Repository
public interface IUserRepository extends IBaseRepository<User, Long> {

    /**
     * Retrieves a user by email.
     *
     * @param email The email to search for.
     * @return The matching user, or {@code null} if not found.
     */
    User findByEmail(String email);
}
