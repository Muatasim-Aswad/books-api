package com.asim.books.author.controller;

import com.asim.books.author.model.dto.AuthorDto;
import com.asim.books.author.service.AuthorService;
import com.asim.books.common.util.SortUtil;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authors")
public class AuthorController implements AuthorControllerDocs {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDto addAuthor(@Valid @RequestBody AuthorDto author) {
        return authorService.addAuthor(author);
    }

    @GetMapping("/{id}")
    public AuthorDto getAuthor(@PathVariable Long id) {
        return authorService.getAuthor(id);
    }

    @PatchMapping("/{id}")
    public AuthorDto updateAuthor(@PathVariable Long id, @Validated(AuthorDto.Optional.class) @RequestBody AuthorDto author) {
        return authorService.updateAuthor(id, author);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
    }

    @GetMapping
    public Page<AuthorDto> getAuthors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort,
            @RequestParam(required = false) String name) {

        Sort sortObj = SortUtil.createObject(sort);
        Pageable pageable = PageRequest.of(page, size, sortObj);

        return authorService.getAuthors(pageable, name);
    }

}