package com.warrantybee.api.repositories.implementations;

import com.warrantybee.api.models.User;
import com.warrantybee.api.repositories.interfaces.IUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

/**
 * Handles custom database operations for {@link User}.
 */
@Repository
public class UserRepository {

    @PersistenceContext
    private EntityManager _entityManager;
    private final IUserRepository _repository;

    /**
     * Initializes the repository with the base interface.
     *
     * @param repository The base user repository.
     */
    public UserRepository(IUserRepository repository) {
        this._repository = repository;
    }

    /**
     * Retrieves a user by email.
     *
     * @param email The email to search for.
     * @return The matching user, or {@code null} if not found.
     */
    public User findByEmail(String email) {
        try {
            String query = "SELECT u FROM User u WHERE u.email = :email";
            return _entityManager.createQuery(query, User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
