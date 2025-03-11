package com.asim.books.author.service;

import com.asim.books.author.model.dto.AuthorDto;

import java.util.List;

public interface AuthorService {
    AuthorDto addAuthor(AuthorDto author);
    AuthorDto getAuthor(Long id);
    AuthorDto updateAuthor(Long id, AuthorDto author);
    void deleteAuthor(Long id);
    List<AuthorDto> getAuthors();
}
