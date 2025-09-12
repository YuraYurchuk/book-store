package com.book.store.mapper;

import com.book.store.config.MapperConfig;
import com.book.store.dto.book.BookDto;
import com.book.store.dto.book.BookDtoWithoutCategoryIds;
import com.book.store.dto.book.CreateBookRequestDto;
import com.book.store.model.Book;
import com.book.store.model.Category;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @Mapping(target = "categoryIds", ignore = true)
    BookDto toDto(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        List<Long> setCategoriesIds = book.getCategories().stream()
                .map(Category::getId)
                .toList();
        bookDto.setCategoryIds(setCategoriesIds);
    }

    @Mapping(target = "categories", ignore = true)
    Book toModel(CreateBookRequestDto requestDto);

    @AfterMapping
    default void setCategory(@MappingTarget Book book, CreateBookRequestDto requestDto) {
        Set<Category> setCategory = requestDto.getCategories().stream()
                .map(Category::new)
                .collect(Collectors.toSet());
        book.setCategories(setCategory);
    }

    void updateBook(CreateBookRequestDto requestDto, @MappingTarget Book book);

    default Category map(Long id) {
        Category category = new Category();
        category.setId(id);
        return category;
    }

    default Set<Category> map(List<Long> value) {
        return value.stream().map(this::map).collect(Collectors.toSet());
    }
}
