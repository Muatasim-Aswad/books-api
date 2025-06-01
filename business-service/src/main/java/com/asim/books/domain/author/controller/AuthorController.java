package com.asim.books.domain.author.controller;

import com.asim.books.common.util.SortUtils;
import com.asim.books.domain.author.model.dto.AuthorDto;
import com.asim.books.domain.author.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/authors")
@RequiredArgsConstructor
public class AuthorController implements AuthorApi {
    private final AuthorService authorService;
    private final SortUtils sortUtils;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDto addAuthor(@RequestBody AuthorDto author) {
        return authorService.addAuthor(author);
    }

    @GetMapping("/{id}")
    public AuthorDto getAuthor(@PathVariable Long id) {
        return authorService.getAuthor(id);
    }

    @PatchMapping("/{id}")
    public AuthorDto updateAuthor(@PathVariable Long id,
                                  @RequestBody AuthorDto author) {
        return authorService.updateAuthor(id, author);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
    }

    @GetMapping
    public Page<AuthorDto> getAuthors(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", required = false) String[] sort,
            @RequestParam(name = "name", required = false) String name) {

        Sort sortObj = sortUtils.createObject(sort, AuthorDto.class);
        Pageable pageable = PageRequest.of(page, size, sortObj);

        return authorService.getAuthors(pageable, name);
    }
}