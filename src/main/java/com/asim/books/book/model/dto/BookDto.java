package com.asim.books.book.model.dto;

import com.asim.books.author.model.dto.AuthorDto;
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
public class BookDto {
    private Long id;
    @NotNull(message = "ISBN cannot be null", groups = Default.class)
    private String isbn;
    @NotBlank(message = "Book title cannot be empty", groups = Default.class)
    @Size(min = 1, max = 255, message = "Book title must be between 1 and 255 characters",
            groups = {Default.class, Optional.class})
    private String title;
    private AuthorDto author;

    // Define optional validation group
    public interface Optional {
    }
}