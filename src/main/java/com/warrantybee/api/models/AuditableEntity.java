package com.warrantybee.api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class AuditableEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "internal_id", updatable = false, insertable = false, nullable = false, columnDefinition = "BINARY(16)")
    private byte[] internalId;

    @Column(name = "created_at", updatable = false, insertable = false, nullable = false)
    private LocalDateTime createdAt;

    @Setter
    @Column(name = "created_by", updatable = false, nullable = false)
    private long createdBy;

    @Column(name = "updated_at", updatable = false, insertable = false)
    private LocalDateTime updatedAt;

    @Setter
    @Column(name = "updated_by", insertable = false)
    private Long updatedBy;

    @Column(name = "void", insertable = false, nullable = false)
    private boolean voided;
}
