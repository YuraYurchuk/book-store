package com.book.store.mapper;

import com.book.store.config.MapperConfig;
import com.book.store.dto.BookDto;
import com.book.store.dto.CreateBookRequestDto;
import com.book.store.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);

    Book updateBook(CreateBookRequestDto requestDto,@MappingTarget Book book);
}
