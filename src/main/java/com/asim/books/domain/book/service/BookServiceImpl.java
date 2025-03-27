package com.asim.books.domain.book.service;

import com.asim.books.common.exception.IllegalAttemptToModify;
import com.asim.books.common.exception.NoIdIsProvidedException;
import com.asim.books.common.exception.ResourceNotFoundException;
import com.asim.books.common.mapper.entity.EntityMapper;
import com.asim.books.domain.author.model.dto.AuthorDto;
import com.asim.books.domain.author.model.entity.Author;
import com.asim.books.domain.book.gateway.AuthorGateway;
import com.asim.books.domain.book.model.dto.BookDto;
import com.asim.books.domain.book.model.entity.Book;
import com.asim.books.domain.book.repository.BookRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * Book service provides the external functionality of:
 * - Creating a new author. (in create book)
 * reason: especially for a new db it's highly likely that the author creation is part of the book creation process.
 * pros: the user don't need multiple requests to perform this coupled operation.
 * cons: introduces a dependency between the book and author services, complicating the book service.
 */
@Service
@Validated
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final EntityMapper<Book, BookDto> bookMapper;
    private final AuthorGateway authorGateway;
    private final EntityManager entityManager;

    /**
     * Can create or assign an existing author to a book.
     */
    @Override
    @Transactional
    @CachePut(value = "books", key = "#result.id")
    public BookDto addBook(BookDto bookDto) {

        AuthorDto author = validateAuthor(bookDto.getAuthor());
        bookDto.setAuthor(author);

        Book book = bookMapper.toEntity(bookDto);

        if (author.getId() != null) {
            //Fix: InvalidDataAccessApiUsageException: detached entity passed to persist
            //Due to cascade.persist, an existing entity is passed to persist.
            Author authorRef = entityManager.getReference(Author.class, author.getId());
            book.setAuthor(authorRef);
        }

        book = bookRepository.save(book);

        entityManager.flush();
        return bookMapper.toDto(book);
    }

    @Override
    @Cacheable(value = "books", key = "#id")
    public BookDto getBook(Long id) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", id));

        return bookMapper.toDto(book);
    }

    /**
     * Can reassign to an existing author.
     */
    @Override
    @Transactional
    @CachePut(value = "books", key = "#id")
    public BookDto updateBook(Long id, BookDto update) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", id));

        if (update.getIsbn() != null) book.setIsbn(update.getIsbn());
        if (update.getTitle() != null) book.setTitle(update.getTitle());

        if (update.getAuthor() != null) {
            AuthorDto author = update.getAuthor();

            //throws if does not exist
            AuthorDto existingAndMatchingAuthor = authorGateway.findMatchingAuthor(author);

            if (existingAndMatchingAuthor == null)
                throw new IllegalAttemptToModify("Author", author.getId(), "An existing author cannot be modified through /books.");

            Author authorRef = entityManager.getReference(Author.class, author.getId());

            book.setAuthor(authorRef);

        }


        book = bookRepository.save(book);

        entityManager.flush();
        return bookMapper.toDto(book);
    }

    @Override
    @CacheEvict(value = "books", key = "#id")
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book", id);
        }
        bookRepository.deleteById(id);
    }

    @Override
    public Page<BookDto> getBooks(Pageable pageable, String title, String author) {
        Specification<Book> spec = Specification.where(null);

        //select * from book where title like %title% and author.name like %author%
        //findByTitleContainingIgnoreCaseAndAuthorNameContainingIgnoreCase

        if (title != null && !title.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
        }

        if (author != null && !author.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("author").get("name")),
                            "%" + author.toLowerCase() + "%"
                    ));
        }

        Page<Book> booksPage = bookRepository.findAll(spec, pageable);
        return booksPage.map(bookMapper::toDto);
    }

    /**
     * Passing this validation means one of these:
     * - This is a valid existing author.
     * - This is a new qualified author.
     * Based on author id:
     * 1- If the id is provided and exists, all other fields should match the existing author or an exception is thrown.
     * 2- If the id is provided and does not exist, an exception is thrown.
     * 3- If the id is not provided, the author is considered new and required fields are validated.
     *
     * @param author the author to validate
     */
    public AuthorDto validateAuthor(AuthorDto author) {
        try {
            AuthorDto existingAndMatchingAuthor = authorGateway.findMatchingAuthor(author);

            if (existingAndMatchingAuthor == null)
                throw new IllegalAttemptToModify("Author", author.getId(), "An existing author cannot be modified through /books.");

            return existingAndMatchingAuthor;

        } catch (NoIdIsProvidedException ex) {
            //the author is considered new as it has no id
            authorGateway.validateAuthorToCreate(author);

            return author;
        }
    }
}