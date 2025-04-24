package com.asim.books.test.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest //used to load the Spring application context
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//used to reset the context after each test method
public abstract class BaseControllerIntegrationTest {
    @Autowired
    protected ObjectMapper objectMapper; //used to convert objects to JSON and vice versa
    @Autowired
    protected MockMvc mockMvc; //used to perform requests and verify responses
}