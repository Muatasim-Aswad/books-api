package com.asim.books.book.service.bookServiceImpl;

import com.asim.books.book.model.dto.BookDto;
import com.asim.books.book.model.entity.Book;
import com.asim.books.book.repository.BookRepository;
import com.asim.books.book.service.BookService;
import com.asim.books.common.exception.custom.ResourceNotFoundException;
import com.asim.books.common.mapper.entity.EntityMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final EntityMapper<Book, BookDto> bookMapper;

    public BookServiceImpl(BookRepository bookRepository, EntityMapper<Book, BookDto> bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public BookDto addBook(BookDto bookDto) {
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