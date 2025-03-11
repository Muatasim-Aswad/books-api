package com.asim.books.author.controller;

import com.asim.books.author.model.dto.AuthorDto;
import com.asim.books.author.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    public ResponseEntity<AuthorDto> addAuthor(@Valid @RequestBody AuthorDto author) {
        AuthorDto authorDto = authorService.addAuthor(author);
        return new ResponseEntity<>(authorDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public AuthorDto getAuthor(@PathVariable Long id) {
        return authorService.getAuthor(id);
    }

    @PatchMapping("/{id}")
    public AuthorDto updateAuthor(@PathVariable Long id, @Validated(AuthorDto.Optional.class) @RequestBody AuthorDto author) {
        return authorService.updateAuthor(id, author);
    }

    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
    }

    @GetMapping
    public List<AuthorDto> getAuthors() {
        return authorService.getAuthors();
    }
}