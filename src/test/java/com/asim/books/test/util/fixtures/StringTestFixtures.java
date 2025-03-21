package com.asim.books.test.util.fixtures;

/**
 * Provides string test fixtures for validation tests covering various edge cases.
 */
public final class StringTestFixtures {
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
}