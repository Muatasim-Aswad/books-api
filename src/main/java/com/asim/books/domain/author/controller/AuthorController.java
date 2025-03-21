package com.asim.books.domain.author.controller;

import com.asim.books.common.util.SortUtils;
import com.asim.books.domain.author.model.dto.AuthorDto;
import com.asim.books.domain.author.service.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authors")
public class AuthorController implements AuthorApi {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

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
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String[] sort,
            @RequestParam(required = false) String name) {

        Sort sortObj = SortUtils.createObject(sort, AuthorDto.class);
        Pageable pageable = PageRequest.of(page, size, sortObj);

        return authorService.getAuthors(pageable, name);
    }
}