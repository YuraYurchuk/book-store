package com.book.store.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.book.store.TestDataFactory;
import com.book.store.dto.category.CategoryDto;
import com.book.store.dto.category.CreateCategoryRequestDto;
import com.book.store.exception.EntityNotFoundException;
import com.book.store.mapper.CategoryMapper;
import com.book.store.model.Category;
import com.book.store.repository.category.CategoryRepository;
import com.book.store.service.impl.CategoryServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Find all saved categories")
    void findAll_findAllSavedCategories_returnCorrectListCategoryDto() {
        Category category = TestDataFactory.getCategory();
        CategoryDto categoryDto = TestDataFactory.getCategoryDto();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> pageCategory = new PageImpl<>(List.of(category), pageable, 1);
        Page<CategoryDto> expected = new PageImpl<>(List.of(categoryDto), pageable, 1);
        when(categoryRepository.findAll(pageable)).thenReturn(pageCategory);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        Page<CategoryDto> actual = categoryService.findAll(pageable);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        verify(categoryRepository, times(1)).findAll(pageable);
        verify(categoryMapper, times(1)).toDto(category);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Find category by Id")
    void getById_findCorrectCategoryById_returnCorrectCategory() {
        Long categoryId = 1L;
        Category category = TestDataFactory.getCategory();
        CategoryDto expected = TestDataFactory.getCategoryDto();
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(expected);

        CategoryDto actual = categoryService.getById(categoryId);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryMapper, times(1)).toDto(category);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Checks if the method will throw an exception when we have wrong category id")
    void getById_findNonExistsCategory_throwException() {
        Long categoryId = 100L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.getById(categoryId)
        );
        String expected = "Can't find category with id " + categoryId;

        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("save category to BD")
    void save_saveCorrectCategory_returnCorrectCategoryDto() {
        CreateCategoryRequestDto requestDto = TestDataFactory.getCategoryRequestDto();
        Category category = TestDataFactory.getCategory();
        CategoryDto expected = TestDataFactory.getCategoryDto();
        when(categoryMapper.toEntity(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expected);

        CategoryDto actual = categoryService.save(requestDto);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        verify(categoryMapper, times(1)).toDto(category);
        verify(categoryMapper, times(1)).toEntity(requestDto);
        verify(categoryRepository, times(1)).save(category);
        verifyNoMoreInteractions(categoryMapper, categoryRepository);
    }

    @Test
    @DisplayName("Update category")
    void update_correctUpdateCategory_returnUpdatedCategoryDto() {
        Long categoryId = 1L;
        CreateCategoryRequestDto requestDto = TestDataFactory.getCategoryRequestDto();
        Category category = TestDataFactory.getCategory();
        CategoryDto expected = TestDataFactory.getCategoryDto();
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expected);

        CategoryDto actual = categoryService.update(categoryId, requestDto);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        verify(categoryMapper, times(1)).toDto(category);
        verify(categoryMapper, times(1)).updateCategory(requestDto, category);
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).save(category);
        verifyNoMoreInteractions(categoryMapper, categoryRepository);
    }

    @Test
    @DisplayName("Checks if the method will throw an exception when we have wrong category id")
    void update_updateNonExistsCategory_throwException() {
        Long categoryId = 100L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.getById(categoryId)
        );
        String expected = "Can't find category with id " + categoryId;
        String actual = exception.getMessage();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Delete category")
    void deleteById_deleteCategory_true() {
        Long categoryId = 1L;

        categoryService.deleteById(categoryId);

        verify(categoryRepository, times(1)).deleteById(categoryId);
        verifyNoMoreInteractions(categoryRepository);
    }
}
