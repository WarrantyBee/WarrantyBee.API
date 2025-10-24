package com.warrantybee.api.models;

import com.warrantybee.api.repositories.DBContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Base entity providing DB operations.
 *
 * @param <T> type of the entity
 */
@Component
public abstract class BaseEntity<T extends BaseEntity<T>> extends AuditableEntity {

    /** Shared database context */
    private static DBContext _context;

    /** Sets the DB context */
    @Autowired
    public void setDbContext(DBContext dbContext) {
        BaseEntity._context = dbContext;
    }

    /** Inserts this entity */
    @Transactional
    public T insert() {
        return (T) _context.insert(this);
    }

    /** Updates this entity */
    @Transactional
    public T update() {
        return (T) _context.update(this);
    }

    /** Deletes this entity */
    @Transactional
    public void delete() {
        _context.delete(this);
    }
}
