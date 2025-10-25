package com.warrantybee.api.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Generic repository for basic CRUD operations using JPA EntityManager.
 */
@Repository
@Transactional
public class DBContext {

    @PersistenceContext
    private EntityManager _entityManager;

    /**
     * Inserts a new entity into the database.
     *
     * @param entity entity to persist
     * @param <T>    entity type
     * @return persisted entity
     */
    public <T> T insert(T entity) {
        _entityManager.persist(entity);
        _entityManager.flush();
        return entity;
    }

    /**
     * Updates an existing entity in the database.
     *
     * @param entity entity to merge
     * @param <T>    entity type
     * @return merged entity
     */
    public <T> T update(T entity) {
        return _entityManager.merge(entity);
    }

    /**
     * Deletes an entity from the database.
     *
     * @param entity entity to remove
     * @param <T>    entity type
     */
    public <T> void delete(T entity) {
        _entityManager.remove(
                _entityManager.contains(entity) ? entity : _entityManager.merge(entity)
        );
    }

    /**
     * Finds an entity by its primary key.
     *
     * @param cls entity class
     * @param id  primary key
     * @param <T> entity type
     * @return found entity or null
     */
    public <T> T findById(Class<T> cls, Object id) {
        return _entityManager.find(cls, id);
    }
}
