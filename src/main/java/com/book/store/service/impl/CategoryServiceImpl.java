package com.book.store.service.impl;

import com.book.store.dto.category.CategoryDto;
import com.book.store.dto.category.CreateCategoryRequestDto;
import com.book.store.exception.EntityNotFoundException;
import com.book.store.mapper.CategoryMapper;
import com.book.store.model.Category;
import com.book.store.repository.category.CategoryRepository;
import com.book.store.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Page<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(categoryMapper::toDto);
    }

    @Override
    public CategoryDto getById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find category with id " + id));
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto save(CreateCategoryRequestDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto update(Long id, CreateCategoryRequestDto categoryDto) {
        Category categoryFromBd = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find category with id " + id));
        categoryMapper.updateCategory(categoryDto, categoryFromBd);
        return categoryMapper.toDto(categoryRepository.save(categoryFromBd));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
