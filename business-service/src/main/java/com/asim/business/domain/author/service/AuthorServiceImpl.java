package com.asim.business.domain.author.service;

import com.asim.business.common.exception.DuplicateResourceException;
import com.asim.business.common.exception.NoIdIsProvidedException;
import com.asim.business.common.exception.OptimisticLockException;
import com.asim.business.common.exception.ResourceNotFoundException;
import com.asim.business.common.model.mapper.EntityDtoMapper;
import com.asim.business.domain.author.model.dto.AuthorDto;
import com.asim.business.domain.author.model.entity.Author;
import com.asim.business.domain.author.repository.AuthorRepository;
import com.asim.business.infrastructure.config.CacheConfigs;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
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
@CacheConfig(cacheNames = CacheConfigs.AUTHORS)
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final EntityDtoMapper<Author, AuthorDto> authorMapper;

    private final EntityManager entityManager;


    @Override
    @Transactional
    @CachePut(key = "#result.id")
    public AuthorDto addAuthor(AuthorDto authorDto) {
        //Check if already exists.
        if (authorRepository.existsByNameAndAge(authorDto.getName(), authorDto.getAge())) {
            throw new DuplicateResourceException("Author", "name & age", authorDto.getName() + "," + authorDto.getAge());
        }

        Author author = authorMapper.toEntity(authorDto);
        author = authorRepository.save(author);

        entityManager.flush(); //flush to get the fields by the db
        return authorMapper.toDto(author);
    }

    @Override
    @Cacheable(key = "#id")
    public AuthorDto getAuthor(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author", id));

        return authorMapper.toDto(author);
    }

    @Override
    @Transactional
    @CachePut(key = "#id")
    public AuthorDto updateAuthor(Long id, AuthorDto update) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author", id));

        // Only update fields that are provided in the DTO
        String name = update.getName();
        Integer age = update.getAge();

        if (name != null) author.setName(name);
        if (age != null) author.setAge(age);

        // Save the updated entity
        author = authorRepository.save(author);

        entityManager.flush();
        return authorMapper.toDto(author);
    }

    @Override
    @Transactional
    @CacheEvict(key = "#id")
    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Author", id);
        }

        authorRepository.deleteById(id);
    }

    @Override
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

    @Override
    public AuthorDto findMatchingAuthor(AuthorDto providedAuthor) {
        // Check if ID is provided & get the author from the database
        Long id = providedAuthor.getId();
        if (id == null) throw new NoIdIsProvidedException("Author");
        AuthorDto dbAuthor = getAuthor(id);

        // Check if versions match
        Integer dbVersion = dbAuthor.getVersion();
        Integer providedVersion = providedAuthor.getVersion();

        if (providedVersion == null)
            throw new OptimisticLockException("Provided author does not have a version number!");
        if (!dbVersion.equals(providedVersion))
            throw new OptimisticLockException("Author has been modified by another user. Current version: "
                    + dbAuthor.getVersion() + ", provided version: " + providedAuthor.getVersion());

        // Check if the provided author contradicts the database author and return
        return dbAuthor.doesContradict(providedAuthor) ? null : dbAuthor;
    }
}
