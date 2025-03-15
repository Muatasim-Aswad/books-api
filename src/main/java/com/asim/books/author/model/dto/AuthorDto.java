package com.asim.books.author.model.dto;

import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorDto {
    @Null(message = "Id must only be specified for existing authors using the path variable")
    private Long id;


    @NotBlank(message = "Author name cannot be empty")
    @Size(min = 2, max = 100, message = "Author name must be between 2 and 100 characters",
            groups = {Default.class, Optional.class})
    private String name;

    @NotNull(message = "Age cannot be null")
    @Min(value = 0, message = "Age must be a positive number",
            groups = {Default.class, Optional.class})
    @Max(value = 150, message = "Age must be less than 150",
            groups = {Default.class, Optional.class})
    private Integer age;

    public AuthorDto(String name, Integer age) {
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