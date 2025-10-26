package com.warrantybee.api.repositories.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Base interface for all repositories, providing common JPA operations.
 *
 * @param <T>  Entity type.
 * @param <ID> Primary key type.
 */
@NoRepositoryBean
public interface IBaseRepository<T, ID> extends JpaRepository<T, ID> {
}
