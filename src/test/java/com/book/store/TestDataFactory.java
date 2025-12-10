package com.book.store;

import com.book.store.dto.book.BookDto;
import com.book.store.dto.book.BookDtoWithoutCategoryIds;
import com.book.store.dto.book.BookSearchParametersDto;
import com.book.store.dto.book.CreateBookRequestDto;
import com.book.store.dto.category.CategoryDto;
import com.book.store.dto.category.CreateCategoryRequestDto;
import com.book.store.model.Book;
import com.book.store.model.Category;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class TestDataFactory {
    public static Book getBook() {
        return new Book()
                .setId(1L)
                .setTitle("Good Book")
                .setAuthor("Yuri")
                .setIsbn("1111111111111")
                .setPrice(new BigDecimal("12.00"))
                .setDescription("Good simple book")
                .setCoverImage("http://example.com/cover3.jpg")
                .setCategories(Set.of(new Category(1L)));
    }

    public static BookDto getBookDto() {
        return new BookDto()
                .setId(1L)
                .setTitle("Good Book")
                .setAuthor("Yuri")
                .setIsbn("1111111111111")
                .setPrice(new BigDecimal("12.00"))
                .setDescription("Good simple book")
                .setCoverImage("http://example.com/cover3.jpg")
                .setCategoryIds(List.of(1L));
    }

    public static BookDto getUpdatedBookDto() {
        return new BookDto()
                .setId(1L)
                .setTitle("Good Book")
                .setAuthor("Yuri")
                .setIsbn("1111111111111")
                .setPrice(new BigDecimal("25.00"))
                .setDescription("updated Good simple book")
                .setCoverImage("http://example.com/cover3.jpg")
                .setCategoryIds(List.of(1L));
    }

    public static CreateBookRequestDto getRequestDto() {
        return new CreateBookRequestDto()
                .setTitle("Good Book")
                .setAuthor("Yuri")
                .setIsbn("1111111111111")
                .setPrice(new BigDecimal("12.00"))
                .setDescription("Good simple book")
                .setCoverImage("http://example.com/cover3.jpg")
                .setCategories(List.of(1L));
    }

    public static CreateBookRequestDto getUpdatedRequestDto() {
        return new CreateBookRequestDto()
                .setTitle("Good Book")
                .setAuthor("Yuri")
                .setIsbn("1111111111111")
                .setPrice(new BigDecimal("25.00"))
                .setDescription("updated Good simple book")
                .setCoverImage("http://example.com/cover3.jpg")
                .setCategories(List.of(1L));
    }

    public static BookSearchParametersDto getSearchParameters() {
        return new BookSearchParametersDto(
                new String[]{"Good Book"},
                new String[]{"Yuri"},
                new String[]{"1111111111111"}
        );
    }

    public static BookSearchParametersDto getBeadSearchParameters() {
        return new BookSearchParametersDto(
                new String[]{"Empty"},
                new String[]{"Nobody"},
                new String[]{"1111"}
        );
    }

    public static BookDtoWithoutCategoryIds getBookWithoutCategory() {
        return new BookDtoWithoutCategoryIds(
                1L, "Good Book", "Yuri", "1111111111111", new BigDecimal("12.00"),
                "Good simple book", "http://example.com/cover3.jpg"
        );
    }

    public static List<BookDto> getThreeBooksDto() {
        return List.of(
                new BookDto()
                        .setId(1L)
                        .setTitle("Good Book 1")
                        .setAuthor("Yuri")
                        .setIsbn("1111111111111")
                        .setPrice(new BigDecimal("12.00"))
                        .setDescription("Good simple book 1")
                        .setCoverImage("http://example.com/cover1.jpg")
                        .setCategoryIds(List.of(1L)),

                new BookDto()
                        .setId(2L)
                        .setTitle("Good Book 2")
                        .setAuthor("Yuri")
                        .setIsbn("2222222222222")
                        .setPrice(new BigDecimal("15.50"))
                        .setDescription("Good simple book 2")
                        .setCoverImage("http://example.com/cover2.jpg")
                        .setCategoryIds(List.of(1L)),

                new BookDto()
                        .setId(3L)
                        .setTitle("Good Book 3")
                        .setAuthor("Yuri")
                        .setIsbn("3333333333333")
                        .setPrice(new BigDecimal("20.00"))
                        .setDescription("Good simple book 3")
                        .setCoverImage("http://example.com/cover3.jpg")
                        .setCategoryIds(List.of(1L))
        );
    }

    public static List<BookDtoWithoutCategoryIds> getThreeBooksWithoutCategory() {
        return List.of(
                new BookDtoWithoutCategoryIds(
                        1L,
                        "Good Book 1",
                        "Yuri",
                        "1111111111111",
                        new BigDecimal("12.00"),
                        "Good simple book 1",
                        "http://example.com/cover1.jpg"
                ),
                new BookDtoWithoutCategoryIds(
                        2L,
                        "Good Book 2",
                        "Yuri",
                        "2222222222222",
                        new BigDecimal("15.50"),
                        "Good simple book 2",
                        "http://example.com/cover2.jpg"
                ),
                new BookDtoWithoutCategoryIds(
                        3L,
                        "Good Book 3",
                        "Yuri",
                        "3333333333333",
                        new BigDecimal("20.00"),
                        "Good simple book 3",
                        "http://example.com/cover3.jpg"
                )
        );
    }

    public static Category getCategory() {
        return new Category()
                .setId(1L)
                .setName("Horror")
                .setDescription("Horror category"
                );
    }

    public static CategoryDto getCategoryDto() {
        return new CategoryDto(
                1L,
                "Horror",
                "Horror category"
        );
    }

    public static CreateCategoryRequestDto getCategoryRequestDto() {
        return new CreateCategoryRequestDto()
                .setName("Horror")
                .setDescription("Horror category");
    }

    public static CreateCategoryRequestDto getUpdatedCategoryRequestDto() {
        return new CreateCategoryRequestDto()
                .setName("Fantasy")
                .setDescription("Fantasy description");
    }

    public static CategoryDto getUpdatedCategoryDto() {
        return new CategoryDto(
                1L,
                "Fantasy",
                "Fantasy description"
        );
    }

    public static List<CategoryDto> getFiveCategoriesDto() {
        return List.of(
                new CategoryDto(1L, "Horror", "Horror description"),
                new CategoryDto(2L, "Fantasy", "Fantasy description"),
                new CategoryDto(3L, "Science Fiction", "Sci-fi description"),
                new CategoryDto(4L, "Romance", "Romance description"),
                new CategoryDto(5L, "Mystery", "Mystery description")
        );
    }
}
