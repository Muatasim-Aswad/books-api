package com.asim.business.domain.author.model.entity;

import com.asim.business.common.model.entity.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder

@Entity
@Table(
        name = "authors",
        indexes = {
                @Index(name = "idx_author_name", columnList = "name")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_author_name_age", columnNames = {"name", "age"})
        })
public class Author extends Auditable {

    @Column(nullable = false)
    private String name;
    private Integer age;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_id_seq")
    @SequenceGenerator(name = "author_id_seq", sequenceName = "author_id_seq", initialValue = 1, allocationSize = 1)
    private Long id;
}
