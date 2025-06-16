package com.book.store.service;

import com.book.store.dto.BookDto;
import com.book.store.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto findById(Long id);

    void deleteBuId(Long id);
}
