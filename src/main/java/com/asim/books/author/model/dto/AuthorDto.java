package com.asim.books.author.model.dto;

import com.asim.books.common.annotation.validation.Age;
import com.asim.books.common.annotation.validation.FullName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

/**
 * Data transfer object for author entities.
 * The following groups are used for external input validation:
 * - Default:
 * - id: null
 * - name: notNull, size(2, 100)
 * - age: notNull, min(0), max(150)
 * - Optional(excludes notNull constraints).
 * * null constraint prevents user from providing values for auto generated fields.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorDto {
    @FullName(groups = {Default.class, Optional.class})
    @NotBlank(message = "Author name cannot be empty")
    private String name;

    @Age(groups = {Default.class, Optional.class})
    @NotNull(message = "Age cannot be null")
    private Integer age;

    //Read only fields
    @Null(message = "Id must only be specified for existing authors resources using the path variable", groups = {Default.class, Optional.class})
    private Long id;
    @Null(message = "Created at is read-only", groups = {Default.class, Optional.class})
    private ZonedDateTime createdAt;
    @Null(message = "Updated at is read-only", groups = {Default.class, Optional.class})
    private ZonedDateTime updatedAt;
    @Null(message = "Created by is read-only", groups = {Default.class, Optional.class})
    private Integer version;

    public AuthorDto(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public AuthorDto(Long id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    /**
     * Checks if the provided author contradicts this one.
     * Only compares non-null fields. The absence of a field is not considered a contradiction.
     *
     * @param author The author to compare
     * @return true if there are contradictions, false otherwise
     */
    public boolean doesContradict(AuthorDto author) {
        if (author == null) {
            return false;
        }

        return contradicts(this.getId(), author.getId()) ||
                contradicts(this.getName(), author.getName()) ||
                contradicts(this.getAge(), author.getAge());
    }

    /**
     * Helper method to check if two values contradict each other.
     * A contradiction exists if both values are non-null and not equal.
     *
     * @param value1 First value to compare
     * @param value2 Second value to compare
     * @return true if values contradict, false otherwise
     */
    private <T> boolean contradicts(T value1, T value2) {
        return value1 != null && !value1.equals(value2);
    }

    /**
     * Validation group for optional fields.
     * Used for partial updates.
     */
    public interface Optional {
    }
}