package com.asim.books.domain.author.service;

import com.asim.books.common.exception.DuplicateResourceException;
import com.asim.books.common.exception.NoIdIsProvidedException;
import com.asim.books.common.exception.OptimisticLockException;
import com.asim.books.common.exception.ResourceNotFoundException;
import com.asim.books.common.mapper.entity.EntityMapper;
import com.asim.books.domain.author.model.dto.AuthorDto;
import com.asim.books.domain.author.model.entity.Author;
import com.asim.books.domain.author.repository.AuthorRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final EntityMapper<Author, AuthorDto> authorMapper;
    private final EntityManager entityManager;


    @Transactional
    @CachePut(value = "authors", key = "#result.id")
    public AuthorDto addAuthor(AuthorDto authorDto) {
        Author author = authorMapper.toEntity(authorDto);

        //or Add Constraint uk_author_name_age UNIQUE (name, age)
        if (authorRepository.existsByNameAndAge(authorDto.getName(), authorDto.getAge())) {
            throw new DuplicateResourceException("Author", "name & age", author.getName() + "," + author.getAge());
        }

        author = authorRepository.save(author);

        entityManager.flush();
        return authorMapper.toDto(author);
    }

    @Cacheable(value = "authors", key = "#id")
    public AuthorDto getAuthor(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author", id));
        return authorMapper.toDto(author);
    }

    @Transactional
    @CachePut(value = "authors", key = "#id")
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

        entityManager.flush();
        return authorMapper.toDto(existingAuthor);
    }

    @Transactional
    @CacheEvict(value = "authors", key = "#id")
    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Author", id);
        }

        authorRepository.deleteById(id);
    }

    public Page<AuthorDto> getAuthors(Pageable pageable, String name) {
        //easier to extend than the query methods
        Specification<Author> spec = Specification.where(null);

        //where name like %name%
        //findByNameContainingIgnoreCase
        if (name != null && !name.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }

        Page<Author> authorsPage = authorRepository.findAll(spec, pageable);
        return authorsPage.map(authorMapper::toDto);
    }

    public boolean authorExists(Long id) {
        return authorRepository.existsById(id);
    }

    public AuthorDto findAuthorAndMatch(AuthorDto providedAuthor) {
        Long id = providedAuthor.getId();
        if (id == null) throw new NoIdIsProvidedException("Author");

        AuthorDto dbAuthor = getAuthor(id);

        Integer dbVersion = dbAuthor.getVersion();
        Integer providedVersion = providedAuthor.getVersion();

        if (providedVersion == null)
            throw new OptimisticLockException("Provided author does not have a version number!");

        if (!dbVersion.equals(providedVersion))
            throw new OptimisticLockException("Author has been modified by another user. Current version: "
                    + dbAuthor.getVersion() + ", provided version: " + providedAuthor.getVersion());

        return dbAuthor.doesContradict(providedAuthor) ? null : dbAuthor;
    }
}
