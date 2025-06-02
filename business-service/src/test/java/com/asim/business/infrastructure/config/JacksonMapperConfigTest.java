package com.asim.business.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.TimeZone;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@DisplayName("Jackson Mapper Configuration Tests")
class JacksonMapperConfigTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("should configure ObjectMapper with UTC timezone when initialized")
    void whenObjectMapperInitialized_thenTimeZoneShouldBeUTC() {
        // Arrange - done by Spring Boot test context

        // Act & Assert
        assertThat(objectMapper.getSerializationConfig().getTimeZone(),
                equalTo(TimeZone.getTimeZone("UTC")));
    }

    @Test
    @DisplayName("should serialize dates as ISO format when writing JSON")
    void whenSerializingDates_thenShouldUseISOFormat() throws Exception {
        // Arrange
        LocalDateTime dateTime = LocalDateTime.of(2023, 5, 15, 10, 30, 0);

        // Act
        String json = objectMapper.writeValueAsString(dateTime);

        // Assert
        assertThat(json, containsString("2023-05-15"));
        assertThat(json, not(containsString("timestamp")));
        assertThat(json, containsString("10:30:00"));
        assertThat(json, not(containsString("+")));  // No timezone offset character

    }

    @Test
    @DisplayName("should have JavaTimeModule registered when initialized")
    void whenObjectMapperInitialized_thenJavaTimeModuleShouldBeRegistered() throws Exception {
        // Arrange
        LocalDateTime dateTime = LocalDateTime.of(2023, 5, 15, 10, 30);

        // Act
        String json = objectMapper.writeValueAsString(dateTime);
        LocalDateTime deserializedDateTime = objectMapper.readValue(json, LocalDateTime.class);

        // Assert
        assertThat(deserializedDateTime, equalTo(dateTime));
    }
}