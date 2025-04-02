package com.asim.books.test.util.fixtures;

import com.asim.books.domain.author.model.dto.AuthorDto;
import com.asim.books.domain.author.model.entity.Author;
import com.asim.books.domain.book.model.dto.BookDto;
import com.asim.books.domain.book.model.entity.Book;

import java.util.stream.Stream;

public final class BookTestFixtures {
    // Valid ISBNs - includes 10 and 13 digit ISBNs
    public static final String[] VALID_ISBNS = {
            "1234567890", "0123456789", "9876543210",
            "1234567890123", "0123456789012", "9876543210987"
    };

    // Invalid ISBNs
    public static final String TOO_SHORT_ISBN = "12345";
    public static final String TOO_LONG_ISBN = "12345678901234567890";
    public static final String NON_NUMERIC_ISBN = "123ABC6789";
    public static final String WITH_HYPHEN_ISBN = "123-456-7890";
    public static final String WITH_SPACES_ISBN = "123 456 7890";
    public static final String WITH_TRAILING_SPACES_ISBN = " 1234567890 ";


    public static final String[] INVALID_ISBNS = {
            TOO_SHORT_ISBN, TOO_LONG_ISBN, NON_NUMERIC_ISBN,
            WITH_HYPHEN_ISBN, WITH_SPACES_ISBN, WITH_TRAILING_SPACES_ISBN,
            CommonTestFixtures.EMPTY_STRING,
            CommonTestFixtures.BLANK_STRING,
            CommonTestFixtures.SPECIAL_CHARACTERS
    };

    // Valid book titles
    public static final String[] VALID_TITLES = {
            "The Great Novel", "Programming in Java", "Spring Boot Essentials",
            "Book with Punctuation: A Tale!", "Numbers 123 in Title",
            "Title with Hyphen - Subtitle"
    };

    // Invalid book titles
    public static final String TOO_LONG_TITLE = "A".repeat(256);
    public static final String INVALID_CHARACTERS_TITLE = "Book with invalid × character";
    public static final String[] INVALID_TITLES = {
            TOO_LONG_TITLE, INVALID_CHARACTERS_TITLE,
            CommonTestFixtures.EMPTY_STRING,
            CommonTestFixtures.BLANK_STRING,
            CommonTestFixtures.TRAILING_WHITESPACE,
            CommonTestFixtures.LEADING_WHITESPACE,
            CommonTestFixtures.SQL_INJECTION
    };

    // Single book
    public static final String ISBN = VALID_ISBNS[0];
    public static final String TITLE = VALID_TITLES[0];

    // Common test data
    public static final String UPDATED_ISBN = VALID_ISBNS[1];
    public static final String UPDATED_TITLE = VALID_TITLES[1];
    public static final String NON_EXISTING_ISBN = "9999999999";

    private BookTestFixtures() {
        // Private constructor to prevent instantiation
    }

    // Factory methods
    public static BookDto createDto(String isbn, String title, AuthorDto authorDto) {
        return BookDto.builder()
                .isbn(isbn)
                .title(title)
                .author(authorDto)
                .build();
    }

    public static Book createEntity(String isbn, String title, Author author, Long id) {
        return Book.builder()
                .isbn(isbn)
                .title(title)
                .author(author)
                .id(id)
                .version(CommonTestFixtures.INTEGER_BOUNDARY_POSITIVE)
                .createdBy(CommonTestFixtures.SMALL_NUMBER)
                .createdAt(CommonTestFixtures.PRESENT_ZONED_DATE_TIME)
                .build();
    }

    public static BookDto getOneDto() {
        return createDto(ISBN, TITLE, AuthorTestFixtures.getOneDto());
    }

    public static BookDto getOneDtoWithId() {
        BookDto bookDto = createDto(ISBN, TITLE, AuthorTestFixtures.getOneDtoWithId());
        bookDto.setId(CommonTestFixtures.POSITIVE_NUMBER);
        return bookDto;
    }

    //get one dto with all fields
    public static BookDto getOneDtoWithAllFields() {
        BookDto bookDto = createDto(ISBN, TITLE, AuthorTestFixtures.getOneDtoWithAllFields());

        bookDto.setId(CommonTestFixtures.POSITIVE_NUMBER);
        bookDto.setVersion(CommonTestFixtures.INTEGER_BOUNDARY_POSITIVE);
        bookDto.setCreatedBy(CommonTestFixtures.SMALL_NUMBER);
        bookDto.setCreatedAt(CommonTestFixtures.PRESENT_ZONED_DATE_TIME);
        bookDto.setLastModifiedBy(CommonTestFixtures.SMALL_NUMBER);
        bookDto.setUpdatedAt(CommonTestFixtures.PRESENT_ZONED_DATE_TIME);

        return bookDto;
    }

    public static BookDto[] getManyDTOs() {
        int length = Math.min(VALID_ISBNS.length, VALID_TITLES.length);
        BookDto[] bookDTOs = new BookDto[length];
        AuthorDto[] authorDTOs = AuthorTestFixtures.getManyDTOs();

        for (int i = 0; i < length; i++) {
            bookDTOs[i] = createDto(
                    VALID_ISBNS[i],
                    VALID_TITLES[i],
                    authorDTOs[i % authorDTOs.length]
            );
        }

        return bookDTOs;
    }

    public static Book getOneEntity() {
        return createEntity(ISBN, TITLE, AuthorTestFixtures.getOneEntity(), CommonTestFixtures.VALID_ID);
    }

    public static Book[] getManyEntities() {
        int length = Math.min(VALID_ISBNS.length, VALID_TITLES.length);
        Book[] books = new Book[length];
        Author[] authors = AuthorTestFixtures.getManyEntities();

        for (int i = 0; i < length; i++) {
            books[i] = createEntity(
                    VALID_ISBNS[i],
                    VALID_TITLES[i],
                    authors[i % authors.length],
                    (long) i + 1
            );
        }

        return books;
    }

    public static BookDto getInvalidIsbnDto() {
        return createDto(TOO_SHORT_ISBN, TITLE, AuthorTestFixtures.getOneDto());
    }

    public static BookDto getInvalidTitleDto() {
        return createDto(ISBN, TOO_LONG_TITLE, AuthorTestFixtures.getOneDto());
    }

    public static BookDto getInvalidAuthorDto() {
        return createDto(ISBN, TITLE, AuthorTestFixtures.getInvalidDto());
    }

    public static BookDto getCompletelyInvalidDto() {
        return createDto(NON_NUMERIC_ISBN, INVALID_CHARACTERS_TITLE, AuthorTestFixtures.getInvalidDto());
    }

    public static Stream<String> invalidIsbnsProvider() {
        return Stream.of(INVALID_ISBNS);
    }

    static Stream<String> invalidTitlesProvider() {
        return Stream.of(INVALID_TITLES);
    }
}