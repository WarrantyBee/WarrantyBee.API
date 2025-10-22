package com.warrantybee.api.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class DBContext {

    @PersistenceContext
    private EntityManager entityManager;

    public <T> T insert(T entity) {
        entityManager.persist(entity);
        entityManager.flush();
        return entity;
    }

    public <T> T update(T entity) {
        return entityManager.merge(entity);
    }

    public <T> void delete(T entity) {
        entityManager.remove(
                entityManager.contains(entity) ?
                        entity :
                        entityManager.merge(entity)
        );
    }

    public <T> T findById(Class<T> cls, Object id) {
        return entityManager.find(cls, id);
    }
}