package com.warrantybee.api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Base class for entities with audit fields.
 */
@Getter
@MappedSuperclass
public abstract class AuditableEntity implements Serializable {

    /** Primary key */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Internal unique identifier */
    @Column(name = "internal_id", updatable = false, insertable = false, nullable = false, columnDefinition = "BINARY(16)")
    private byte[] internalId;

    /** Timestamp when the entity was created */
    @Column(name = "created_at", updatable = false, insertable = false, nullable = false)
    private LocalDateTime createdAt;

    /** ID of the user who created the entity */
    /*@Setter
    @Column(name = "created_by", updatable = false, nullable = false)
    private long createdBy;*/

    /** Timestamp when the entity was last updated */
    @Column(name = "updated_at", updatable = false, insertable = false)
    private LocalDateTime updatedAt;

    /** ID of the user who last updated the entity */
    /*@Setter
    @Column(name = "updated_by", insertable = false)
    private Long updatedBy;*/

    /** Soft-delete flag */
    @Column(name = "void", insertable = false, nullable = false)
    private boolean voided;
}
