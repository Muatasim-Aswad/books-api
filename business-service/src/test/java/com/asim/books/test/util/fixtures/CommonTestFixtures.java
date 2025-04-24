package com.asim.books.test.util.fixtures;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Provides test fixtures for validation tests covering various edge cases.
 */
public final class CommonTestFixtures {
    // Empty and blank strings
    public static final String NULL_STRING = null;
    public static final String EMPTY_STRING = "";
    public static final String BLANK_STRING = " ";
    public static final String MULTIPLE_SPACES = "     ";

    // Whitespace variations
    public static final String LEADING_WHITESPACE = "  leading";
    public static final String TRAILING_WHITESPACE = "trailing  ";
    public static final String LEADING_AND_TRAILING_WHITESPACE = "  middle  ";
    public static final String INTERNAL_WHITESPACE = "text with spaces";
    public static final String TAB_CHARACTER = "text\twith\ttabs";
    public static final String NEWLINE_CHARACTER = "text\nwith\nnewlines";

    // Special characters
    public static final String SPECIAL_CHARACTERS = "!@#$%^&*()_+";
    public static final String HTML_TAGS = "<script>alert('XSS')</script>";
    public static final String SQL_INJECTION = "'; DROP TABLE users; --";
    public static final String EMOJI = "Hello 😊 World 🌍";

    // Length cases
    public static final String SINGLE_CHARACTER = "a";
    public static final String VERY_LONG_STRING = "a".repeat(1000);

    // Unicode and non-ASCII
    public static final String NON_ASCII = "áéíóúñÁÉÍÓÚÑ";
    public static final String CHINESE_CHARACTERS = "你好世界";
    public static final String ARABIC_CHARACTERS = "مرحبا بالعالم";

    // Common validation patterns
    public static final String VALID_EMAIL = "user@example.com";
    public static final String INVALID_EMAIL = "not-an-email";
    public static final String VALID_URL = "https://example.com";
    public static final String INVALID_URL = "not-a-url";
    public static final String VALID_PHONE = "+1-555-123-4567";
    public static final String INVALID_PHONE = "phone";

    // Numeric strings
    public static final String NUMERIC_STRING = "12345";
    public static final String ALPHANUMERIC = "abc123";
    public static final String DECIMAL_STRING = "123.45";
    public static final String NEGATIVE_NUMBER_STRING = "-123";

    // ids
    public static final Long VALID_ID = 1L;
    public static final Long NEGATIVE_ID = -5L;
    public static final Long ZERO_ID = 0L;
    public static final Long NON_EXISTING_ID = 1000000000000000000L;
    public static final String STRING_ID = "invalid-id";

    // numbers (not strings)
    public static final Long NEGATIVE_NUMBER = -5L;
    public static final Long ZERO = 0L;
    public static final Long POSITIVE_NUMBER = 5L;
    public static final Long LARGE_NUMBER = 1000000000000000000L;
    public static final Long SMALL_NUMBER = 1L;
    public static final Long NULL_NUMBER = null;

    // Common boundary values for integers
    public static final Integer INTEGER_MIN_VALUE = Integer.MIN_VALUE;
    public static final Integer INTEGER_MAX_VALUE = Integer.MAX_VALUE;
    public static final Integer INTEGER_BOUNDARY_NEGATIVE = -1;
    public static final Integer INTEGER_BOUNDARY_POSITIVE = 1;
    public static final Integer INTEGER_NULL = null;


    // Boolean values
    public static final Boolean TRUE_VALUE = true;
    public static final Boolean FALSE_VALUE = false;
    public static final Boolean NULL_BOOLEAN = null;

    // Collection fixtures
    public static final List<String> EMPTY_LIST = Collections.emptyList();
    public static final List<String> SINGLE_ITEM_LIST = Collections.singletonList("item");
    public static final List<String> MULTIPLE_ITEMS_LIST = List.of("item1", "item2", "item3");

    // UUID pattern strings
    public static final String VALID_UUID_STRING = "123e4567-e89b-12d3-a456-426614174000";
    public static final String INVALID_UUID_STRING = "not-a-uuid";
    public static final UUID RANDOM_UUID = UUID.randomUUID();

    // Date/time fixtures
    public static final LocalDate PAST_DATE = LocalDate.now().minusYears(1);
    public static final LocalDate PRESENT_DATE = LocalDate.now();
    public static final LocalDate FUTURE_DATE = LocalDate.now().plusYears(1);
    public static final LocalDateTime PAST_DATE_TIME = LocalDateTime.now().minusHours(24);
    public static final LocalDateTime PRESENT_DATE_TIME = LocalDateTime.now();
    public static final LocalDateTime FUTURE_DATE_TIME = LocalDateTime.now().plusHours(24);
    public static final ZonedDateTime PAST_ZONED_DATE_TIME = ZonedDateTime.now().minusDays(1);
    public static final ZonedDateTime PRESENT_ZONED_DATE_TIME = ZonedDateTime.now();
    public static final ZonedDateTime FUTURE_ZONED_DATE_TIME = ZonedDateTime.now().plusDays(1);

    private CommonTestFixtures() {
    }
}