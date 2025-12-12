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
import com.book.store.dto.book.BookDtoWithoutCategoryIds;
import com.book.store.dto.category.CategoryDto;
import com.book.store.dto.category.CreateCategoryRequestDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
class CategoryControllerTest {
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
    @DisplayName("save category to DB")
    void createCategory_saveCorrectCategory_returnCategoryDto() throws Exception {
        CreateCategoryRequestDto requestDto = TestDataFactory.getCategoryRequestDto();
        CategoryDto expected = TestDataFactory.getCategoryDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(
                        post("/categories")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class
        );

        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("get all categories from DB")
    @Sql(scripts = "classpath:database/add-five-categories-to-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getAll_findCorrectCategoriesFromDb_returnCorrectPageOfCategoryDto() throws Exception {
        List<CategoryDto> categoryDtoList = TestDataFactory.getFiveCategoriesDto();
        Pageable pageable = PageRequest.of(0, 10);
        Page<CategoryDto> expected = new PageImpl<>(categoryDtoList, pageable, 5);
        MvcResult result = mockMvc.perform(
                        get("/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        JsonNode node = objectMapper.readTree(result.getResponse().getContentAsString());
        CategoryDto[] categoryDtoArray = objectMapper.treeToValue(
                node.get("content"), CategoryDto[].class);
        Page<CategoryDto> actual = new PageImpl<>(
                Arrays.stream(categoryDtoArray).toList(), pageable, 5);
        Assertions.assertThat(
                        actual.getContent())
                .usingRecursiveComparison()
                .isEqualTo(expected.getContent()
                );
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("get all categories from empty DB")
    void getAll_emptyDb_returnEmptyPage() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/categories")
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
    @DisplayName("get category by id")
    @Sql(scripts = "classpath:database/add-category-to-table-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getCategoryById_findCorrectCategory_returnCorrectCategoryDto() throws Exception {
        long categoryId = 1L;
        CategoryDto expected = TestDataFactory.getCategoryDto();
        MvcResult result = mockMvc.perform(
                        get("/categories/" + categoryId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("get non-exist category")
    void getCategoryById_FindNonExistCategory_throwException() throws Exception {
        long categoryId = 999L;
        MvcResult result = mockMvc.perform(
                        get("/categories/" + categoryId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andReturn();
        String errorMessage = result.getResponse().getContentAsString();
        Assertions.assertThat(errorMessage).isEqualTo(
                "Entity not found exception occurred. Can't find category with id " + categoryId
        );
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("update correct category")
    @Sql(scripts = "classpath:database/add-category-to-table-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void updateCategory_updateCorrectCategory_returnUpdatedCategoryDto() throws Exception {
        CreateCategoryRequestDto requestDto = TestDataFactory.getUpdatedCategoryRequestDto();
        CategoryDto expected = TestDataFactory.getUpdatedCategoryDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        long categoryId = 1L;
        MvcResult result = mockMvc.perform(
                        put("/categories/" + categoryId)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("update non-exist category")
    void updateCategory_nonExistCategory_throwException() throws Exception {
        CreateCategoryRequestDto requestDto = TestDataFactory.getUpdatedCategoryRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        long categoryId = 999L;
        MvcResult result = mockMvc.perform(
                        put("/categories/" + categoryId)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andReturn();
        String errorMessage = result.getResponse().getContentAsString();
        Assertions.assertThat(errorMessage).isEqualTo(
                "Entity not found exception occurred. Can't find category with id " + categoryId
        );
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("delete correct category")
    @Sql(scripts = "classpath:database/add-category-to-table-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteCategory_deleteCorrectCategory_httpStatusNoContent() throws Exception {
        long categoryId = 1L;
        mockMvc.perform(delete("/categories/" + categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
        MvcResult result = mockMvc.perform(get("/categories/" + categoryId)
                        .with(user("user").roles("USER")))
                .andExpect(status().isNotFound())
                .andReturn();
        String errorMessage = result.getResponse().getContentAsString();
        Assertions.assertThat(errorMessage).isEqualTo(
                "Entity not found exception occurred. Can't find category with id " + categoryId
        );
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("get books by category id")
    @Sql(scripts = {"classpath:database/add-five-categories-to-categories-table.sql",
            "classpath:database/add-five-books-with-different-categories-to-books-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getBooksByCategoryId_findTreeBooks_returnCorrectListOfTreeBookDto() throws Exception {
        long categoryId = 1L;
        List<BookDtoWithoutCategoryIds> expected = TestDataFactory.getThreeBooksWithoutCategory();
        MvcResult result = mockMvc.perform(
                        get("/categories/" + categoryId + "/books")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        BookDtoWithoutCategoryIds[] booksArray = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDtoWithoutCategoryIds[].class);
        List<BookDtoWithoutCategoryIds> actual = Arrays.stream(booksArray).toList();
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("get books by bed category id")
    @Sql(scripts = {"classpath:database/add-five-categories-to-categories-table.sql",
            "classpath:database/add-five-books-with-different-categories-to-books-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getBooksByCategoryId_bedCategoryId_returnEmptyList() throws Exception {
        long categoryId = 999L;
        List<BookDtoWithoutCategoryIds> expected = Collections.emptyList();
        MvcResult result = mockMvc.perform(
                        get("/categories/" + categoryId + "/books")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        BookDto[] bookDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto[].class
        );
        BookDtoWithoutCategoryIds[] bookArray = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDtoWithoutCategoryIds[].class
        );
        List<BookDtoWithoutCategoryIds> actual = Arrays.stream(bookArray).toList();
        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
