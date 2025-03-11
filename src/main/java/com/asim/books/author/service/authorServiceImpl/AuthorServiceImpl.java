package com.asim.books.author.service.authorServiceImpl;

import com.asim.books.author.model.dto.AuthorDto;
import com.asim.books.author.model.entity.Author;
import com.asim.books.author.repository.AuthorRepository;
import com.asim.books.author.service.AuthorService;
import com.asim.books.common.mapper.entity.EntityMapper;
import com.asim.books.common.exception.custom.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final EntityMapper<Author, AuthorDto> authorMapper;

    public AuthorServiceImpl(AuthorRepository authorRepository, EntityMapper<Author, AuthorDto> authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    public AuthorDto addAuthor(AuthorDto authorDto) {
        Author author = authorMapper.toEntity(authorDto);
        author = authorRepository.save(author);
        return authorMapper.toDto(author);
    }

    public AuthorDto getAuthor(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author", id));
        return authorMapper.toDto(author);
    }

    public AuthorDto updateAuthor(Long id, AuthorDto authorDto) {
        Author existingAuthor = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author", id));

        // Only update fields that are provided in the DTO
        if (authorDto.getName() != null) {
            existingAuthor.setName(authorDto.getName());
        }

        if (authorDto.getAge() != null) {
            existingAuthor.setAge(authorDto.getAge());
        }

        // Save the updated entity
        existingAuthor = authorRepository.save(existingAuthor);
        return authorMapper.toDto(existingAuthor);
    }

    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Author", id);
        }

        authorRepository.deleteById(id);
    }

    public List<AuthorDto> getAuthors() {
        List<Author> authors = authorRepository.findAll();
        return authors.
                stream().
                map(authorMapper::toDto).
                toList();
    }
}
