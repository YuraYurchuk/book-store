package com.book.store.repository.book;

import com.book.store.TestDataFactory;
import com.book.store.dto.book.BookDtoWithoutCategoryIds;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class BookRepositoryTest {
    @Container
    private static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.32");
    @Autowired
    private BookRepository bookRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        // Примусово вмикаємо Liquibase для цього тесту
        registry.add("spring.liquibase.enabled", () -> "true");
    }

    @Test
    @DisplayName("Find all books by category id")
    @Sql(scripts = {
            "classpath:database/add-category-to-table-categories.sql",
            "classpath:database/add-book-to-book-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findAllByCategoriesId_correctDataInput_returnCorrectListOfBooks() {
        BookDtoWithoutCategoryIds expected = TestDataFactory.getBookWithoutCategory();

        List<BookDtoWithoutCategoryIds> actual = bookRepository.findAllByCategories_Id(1L);

        Assertions.assertThat(actual.get(0)).usingRecursiveComparison().isEqualTo(expected);
    }
}
