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
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

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
