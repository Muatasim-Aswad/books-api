//package com.asim.books.book.controller.integration;
//
//import com.asim.books.book.model.dto.BookDto;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import static org.hamcrest.Matchers.is;
//import static org.hamcrest.Matchers.notNullValue;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//
//public class PostBookIntegrationTests extends BaseBookControllerIntegrationTest {
//
//    @Test
//    @DisplayName("Should return 201 when book is valid")
//    void addBook_WhenBookIsValid_ThenReturn201() throws Exception {
//        // Given
//        BookDto book = BookDto.builder()
//                .title("The Great Gatsby")
//                .isbn("1234567890")
//                .authorId(1L)
//                .build();
//        String bookJson = objectMapper.writeValueAsString(book);
//
//        // When/Then
//        mockMvc.perform(MockMvcRequestBuilders
//                .post(BASE_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(bookJson))
//                .andExpect(MockMvcResultMatchers.status().isCreated())
//                .andExpect(jsonPath("$.id", notNullValue()))
//                .andExpect(jsonPath("$.title", is("The Great Gatsby")))
//                .andExpect(jsonPath("$.isbn", is("1234567890")))
//                .andExpect(jsonPath("$.authorId", is(1)));
//    }
//
//    @Test
//    @DisplayName("Should return 400 when book is invalid")
//    void addBook_WhenBookIsInvalid_ThenReturn400() throws Exception {
//        // Given
//        BookDto book = BookDto.builder()
//                .title("") // Empty title, should fail validation
//                .isbn("1234567890")
//                .authorId(1L)
//                .build();
//        String bookJson = objectMapper.writeValueAsString(book);
//
//        // When/Then
//        mockMvc.perform(MockMvcRequestBuilders
//                .post(BASE_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(bookJson))
//                .andExpect(MockMvcResultMatchers.status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("Should return 400 when book is null")
//    void addBook_WhenBookIsNull_ThenReturn400() throws Exception {
//        // Given
//        String bookJson = ""; // Empty body
//
//        // When/Then
//        mockMvc.perform(MockMvcRequestBuilders
//                .post(BASE_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(bookJson))
//                .andExpect(MockMvcResultMatchers.status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("Should return 409 when book is duplicate")
//    void addBook_WhenBookIsDuplicate_ThenReturn409() throws Exception {
//        // Given
//        BookDto book = BookDto.builder()
//                .title("To Kill a Mockingbird")
//                .isbn("0987654321")
//                .authorId(2L)
//                .build();
//        String bookJson = objectMapper.writeValueAsString(book);
//
//        // First, add the book
//        mockMvc.perform(MockMvcRequestBuilders
//                .post(BASE_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(bookJson))
//                .andExpect(MockMvcResultMatchers.status().isCreated());
//
//        // When/Then - Try to add the same book again
//        mockMvc.perform(MockMvcRequestBuilders
//                .post(BASE_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(bookJson))
//                .andExpect(MockMvcResultMatchers.status().isConflict());
//    }
//}