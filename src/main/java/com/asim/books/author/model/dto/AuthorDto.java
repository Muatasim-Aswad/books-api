package com.asim.books.author.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    // Define optional validation group
    public interface Optional {}

    private Long id;

    // Required in default group, but just validated if present in Optional group
    @NotBlank(message = "Author name cannot be empty", groups = Default.class)
    @Size(min = 2, max = 100, message = "Author name must be between 2 and 100 characters",
            groups = {Default.class, Optional.class})
    private String name;

    // Required in default group, but just validated if present in Optional group
    @NotNull(message = "Age cannot be null", groups = Default.class)
    @Min(value = 0, message = "Age must be a positive number",
            groups = {Default.class, Optional.class})
    private Integer age;

    public AuthorDto(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}