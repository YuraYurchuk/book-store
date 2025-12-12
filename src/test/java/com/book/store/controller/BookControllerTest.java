package com.book.store.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.book.store.TestDataFactory;
import com.book.store.custom.CustomMySqlContainer;
import com.book.store.dto.book.BookDto;
import com.book.store.dto.book.CreateBookRequestDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.sql.DataSource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    @Container
    private static final CustomMySqlContainer mysql = CustomMySqlContainer.getInstance();
    private static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    void tearDown(@Autowired DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/delete-all-table.sql")
            );
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("save book to DB")
    @Sql(scripts = "classpath:database/add-category-to-table-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void save_saveCorrectBook_returnCorrectBookDto() throws Exception {
        CreateBookRequestDto requestDto = TestDataFactory.getRequestDto();
        BookDto expected = TestDataFactory.getBookDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(
                        post("/books")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BookDto.class
        );

        Assertions.assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id").isEqualTo(expected);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("get all books from DB return current list of books")
    @Sql(scripts = {
            "classpath:database/add-category-to-table-categories.sql",
            "classpath:database/add-three-books-to-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findAll_findThreeBooks_returnListOfBookDto() throws Exception {
        List<BookDto> books = TestDataFactory.getThreeBooksDto();
        Pageable pageable = PageRequest.of(0, 10);
        Page<BookDto> expected = new PageImpl<>(books, pageable, 3);
        MvcResult result = mockMvc.perform(
                        get("/books")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        JsonNode node = objectMapper.readTree(result.getResponse().getContentAsString());
        BookDto[] bookDtoArray = objectMapper.treeToValue(node.get("content"), BookDto[].class);
        Page<BookDto> actual = new PageImpl<>(Arrays.stream(bookDtoArray).toList(), pageable, 3);

        Assertions.assertThat(
                        actual.getContent())
                .usingRecursiveComparison()
                .withComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                .isEqualTo(expected.getContent());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("find all return empty list")
    void findAll_emptyResult_returnEmptyList() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/books")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        JsonNode node = objectMapper.readTree(result.getResponse().getContentAsString());
        JsonNode nodeContent = node.get("content");

        Assertions.assertThat(nodeContent).hasSize(0);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("get current book from DB by Id")
    @Sql(scripts = {
            "classpath:database/add-category-to-table-categories.sql",
            "classpath:database/add-book-to-book-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getById_correctId_returnCorrectBookDto() throws Exception {
        long bookId = 1L;
        BookDto expected = TestDataFactory.getBookDto();
        MvcResult result = mockMvc.perform(
                        get("/books/" + bookId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BookDto.class
        );

        Assertions.assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id").isEqualTo(expected);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("find non-existent book")
    void getById_nonExistBook_throwException() throws Exception {
        long bookId = 999L;
        MvcResult result = mockMvc.perform(
                        get("/books/" + bookId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andReturn();

        String errorMessage = result.getResponse().getContentAsString();

        Assertions.assertThat(errorMessage)
                .isEqualTo("Entity not found exception occurred. Can't find book by id " + bookId);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("delete book from DB")
    @Sql(scripts = {
            "classpath:database/add-category-to-table-categories.sql",
            "classpath:database/add-book-to-book-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteById_deleteBook_true() throws Exception {
        long bookId = 1L;
        mockMvc.perform(delete("/books/" + bookId))
                .andExpect(status().isNoContent())
                .andReturn();
        MvcResult result = mockMvc.perform(get("/books/" + bookId)
                        .with(user("user").roles("USER")))
                .andExpect(status().isNotFound())
                .andReturn();

        String errorMessage = result.getResponse().getContentAsString();

        Assertions.assertThat(errorMessage)
                .isEqualTo("Entity not found exception occurred. Can't find book by id " + bookId);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("update book from DB")
    @Sql(scripts = {
            "classpath:database/add-category-to-table-categories.sql",
            "classpath:database/add-book-to-book-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void updateById_updateCorrectBook_returnCorrectBookDto() throws Exception {
        CreateBookRequestDto requestDto = TestDataFactory.getUpdatedRequestDto();
        BookDto expected = TestDataFactory.getUpdatedBookDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        long bookId = 1L;
        MvcResult result = mockMvc.perform(
                        put("/books/" + bookId)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class
        );

        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("update non exist book")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateById_updateNonExistBook_throwException() throws Exception {
        CreateBookRequestDto requestDto = TestDataFactory.getUpdatedRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        long bookId = 999L;
        MvcResult result = mockMvc.perform(put(
                        "/books/" + bookId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andReturn();

        String message = result.getResponse().getContentAsString();

        Assertions.assertThat(message)
                .isEqualTo("Entity not found exception occurred. Can't find book by id " + bookId);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("find books by search parameters")
    @Sql(scripts = {
            "classpath:database/add-category-to-table-categories.sql",
            "classpath:database/add-five-books-to-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void search_searchBooksByCorrectParameters_returnListOfBooks() throws Exception {
        List<BookDto> expected = TestDataFactory.getThreeBooksDto();
        MvcResult result = mockMvc.perform(
                        get("/books/search")
                                .param("author", "Yuri")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDto[] bookDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto[].class
        );
        List<BookDto> actual = Arrays.stream(bookDto).toList();

        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("find books by bead search parameters")
    @Sql(scripts = {
            "classpath:database/add-category-to-table-categories.sql",
            "classpath:database/add-five-books-to-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void search_nonCorrectParameters_returnEmptyList() throws Exception {
        List<BookDto> expected = Collections.emptyList();
        MvcResult result = mockMvc.perform(
                        get("/books/search")
                                .param("author", "nobody")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        BookDto[] bookDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto[].class
        );

        List<BookDto> actual = Arrays.stream(bookDto).toList();

        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
