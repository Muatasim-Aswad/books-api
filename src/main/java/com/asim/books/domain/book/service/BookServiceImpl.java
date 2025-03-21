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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final EntityMapper<Book, BookDto> bookMapper;
    private final AuthorGateway authorGateway;

    public BookServiceImpl(BookRepository bookRepository,
                           EntityMapper<Book, BookDto> bookMapper,
                           AuthorGateway authorGateway) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.authorGateway = authorGateway;
    }

    @Override
    public BookDto addBook(BookDto bookDto) {

        validateAuthor(bookDto.getAuthor());

        Book book = bookMapper.toEntity(bookDto);
        book = bookRepository.save(book);

        return bookMapper.toDto(book);
    }

    @Override
    public BookDto getBook(Long id) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", id));

        return bookMapper.toDto(book);
    }

    @Override
    public BookDto updateBook(Long id, BookDto update) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", id));

        if (update.getIsbn() != null) book.setIsbn(update.getIsbn());
        if (update.getTitle() != null) book.setTitle(update.getTitle());

        if (update.getAuthor() != null) {
            AuthorDto updateAuthor = update.getAuthor();
            //validate author
            validateAuthor(updateAuthor);

            if (updateAuthor.getId() != null) {
                //change the author to a different author
                Long authorId = updateAuthor.getId();
                book.setAuthor(
                        Author.builder()
                                .id(authorId).build());
            } else {
                //create a new author and assign it to the book
                Author newAuthor = Author.builder()
                        .name(updateAuthor.getName())
                        .age(updateAuthor.getAge())
                        .build();
                book.setAuthor(newAuthor);
            }
        }


        book = bookRepository.save(book);
        return bookMapper.toDto(book);
    }

    @Override
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
    public void validateAuthor(@Validated(AuthorDto.Required.class) AuthorDto author) {
        try {
            boolean isAuthorExistAndMatch = authorGateway.findAuthorAndMatch(author);

            if (!isAuthorExistAndMatch)
                throw new IllegalAttemptToModify("Author", author.getId(), "An existing author cannot be modified through /books.");

        } catch (NoIdIsProvidedException ex) {
            //the author is considered new
            authorGateway.validateAuthorRequired(author);
        }
    }
}