package com.asim.business.common.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

/**
 * This class is the base class for all entities that need to be fully audited.
 * It contains the fields createdBy and lastModifiedBy.
 * It inherits from TimeAuditable to get the createdAt, updatedAt, and version fields.
 */
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter

@MappedSuperclass
public abstract class Auditable extends TimeAuditable {
    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    private Long createdBy;

    @LastModifiedBy
    @Column(name = "last_modified_by")
    private Long lastModifiedBy;
}