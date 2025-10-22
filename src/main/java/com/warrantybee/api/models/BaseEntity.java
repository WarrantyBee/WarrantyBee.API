package com.warrantybee.api.models;

import com.warrantybee.api.repositories.DBContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public abstract class BaseEntity<T extends BaseEntity<T>> extends AuditableEntity {
    private static DBContext _context;

    @Autowired
    public void setDbContext(DBContext dbContext) {
        BaseEntity._context = dbContext;
    }

    @Transactional
    public T insert() {
        return (T) _context.insert(this);
    }

    @Transactional
    public T update() {
        return (T) _context.update(this);
    }

    @Transactional
    public void delete() {
        _context.delete(this);
    }
}
