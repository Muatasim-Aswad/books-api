package com.asim.books.domain.book.controller;

import com.asim.books.common.util.SortUtils;
import com.asim.books.domain.book.model.dto.BookDto;
import com.asim.books.domain.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController implements BookApi {
    private final BookService bookService;
    private final SortUtils sortUtils;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto addBook(@RequestBody BookDto book) {
        return bookService.addBook(book);
    }

    @GetMapping("/{id}")
    public BookDto getBook(@PathVariable Long id) {
        return bookService.getBook(id);
    }

    @PatchMapping("/{id}")
    public BookDto updateBook(@PathVariable Long id, @RequestBody BookDto book) {
        return bookService.updateBook(id, book);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }

    @GetMapping
    public Page<BookDto> getBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String[] sort,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author) {

        Sort sortObj = sortUtils.createObject(sort, BookDto.class);
        Pageable pageable = PageRequest.of(page, size, sortObj);

        return bookService.getBooks(pageable, title, author);
    }
}