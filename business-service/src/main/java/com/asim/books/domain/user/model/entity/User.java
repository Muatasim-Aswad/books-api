package com.asim.books.domain.user.model.entity;

import com.asim.books.common.model.entity.TimeAuditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

@Entity
@Table(name = "users",
        indexes = {
                @Index(name = "idx_user_name", columnList = "name")
        }
)
public class User extends TimeAuditable {
    @Id
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}
