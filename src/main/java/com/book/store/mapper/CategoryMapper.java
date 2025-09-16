package com.book.store.mapper;

import com.book.store.config.MapperConfig;
import com.book.store.dto.category.CategoryDto;
import com.book.store.dto.category.CreateCategoryRequestDto;
import com.book.store.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toEntity(CreateCategoryRequestDto categoryDto);

    void updateCategory(CreateCategoryRequestDto categoryDto, @MappingTarget Category category);
}
