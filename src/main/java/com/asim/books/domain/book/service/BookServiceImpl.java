package com.asim.books.domain.book.service;

import com.asim.books.common.exception.IllegalAttemptToModify;
import com.asim.books.common.exception.NoIdIsProvidedException;
import com.asim.books.common.exception.ResourceNotFoundException;
import com.asim.books.common.mapper.entity.EntityMapper;
import com.asim.books.domain.author.model.dto.AuthorDto;
import com.asim.books.domain.book.gateway.AuthorGateway;
import com.asim.books.domain.book.model.dto.BookDto;
import com.asim.books.domain.book.model.entity.Book;
import com.asim.books.domain.book.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final EntityMapper<Book, BookDto> bookMapper;
    private final AuthorGateway authorGatewayImpl;

    public BookServiceImpl(BookRepository bookRepository, EntityMapper<Book, BookDto> bookMapper, AuthorGateway authorGatewayImpl) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.authorGatewayImpl = authorGatewayImpl;
    }

    @Override
    public BookDto addBook(BookDto bookDto) throws IllegalAttemptToModify {
        AuthorDto author = bookDto.getAuthor();

        try {
            boolean authorAndMatch = authorGatewayImpl.findAuthorAndMatch(author);
            if (!authorAndMatch) {
                throw new IllegalAttemptToModify("Author", author.getId(), "An existing author cannot be modified through /books.");
            }
        } catch (NoIdIsProvidedException ignored) {
        }

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
    public BookDto updateBook(Long id, BookDto bookDto) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", id));

        if (bookDto.getIsbn() != null) {
            existingBook.setIsbn(bookDto.getIsbn());
        }
        if (bookDto.getTitle() != null) {
            existingBook.setTitle(bookDto.getTitle());
        }
        if (bookDto.getAuthor() != null) {
            existingBook.setAuthor(bookMapper.toEntity(bookDto).getAuthor());
        }

        existingBook = bookRepository.save(existingBook);
        return bookMapper.toDto(existingBook);
    }

    @Override
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book", id);
        }
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDto> getBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(bookMapper::toDto)
                .toList();
    }
}