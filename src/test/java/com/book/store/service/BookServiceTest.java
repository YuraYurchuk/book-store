package com.book.store.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.book.store.TestDataFactory;
import com.book.store.dto.book.BookDto;
import com.book.store.dto.book.BookDtoWithoutCategoryIds;
import com.book.store.dto.book.BookSearchParametersDto;
import com.book.store.dto.book.CreateBookRequestDto;
import com.book.store.exception.EntityNotFoundException;
import com.book.store.mapper.BookMapper;
import com.book.store.model.Book;
import com.book.store.repository.book.BookRepository;
import com.book.store.repository.book.BookSpecificationBuilder;
import com.book.store.service.impl.BookServiceImpl;
import java.util.Collections;
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
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookSpecificationBuilder specificationBuilder;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Find Book by correct ID")
    void findById_findBookByCorrectId_returnCorrectBook() {
        Long bookId = 1L;
        Book book = TestDataFactory.getBook();
        BookDto expected = TestDataFactory.getBookDto();
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expected);

        BookDto actual = bookService.findById(bookId);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("Checks if the method will throw an exception when we have wrong book Id")
    void findById_findNonExistBook_throwException() {
        Long bookId = 100L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.findById(bookId)
        );

        String expected = "Can't find book by id " + bookId;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Save correct book")
    void save_saveCorrectBook_returnSavedBook() {
        CreateBookRequestDto requestDto = TestDataFactory.getRequestDto();
        Book book = TestDataFactory.getBook();
        BookDto bookDto = TestDataFactory.getBookDto();
        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto saveBookDto = bookService.save(requestDto);

        assertThat(saveBookDto).usingRecursiveComparison().isEqualTo(bookDto);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).toModel(requestDto);
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Find all book from DB")
    void findAll_findAllBook_returnCorrectListOfBooks() {
        Book book = TestDataFactory.getBook();
        BookDto bookDto = TestDataFactory.getBookDto();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> pageBooks = new PageImpl<>(List.of(book), pageable,1);
        Page<BookDto> expected = new PageImpl<>(List.of(bookDto), pageable, 1);
        when(bookRepository.findAll(pageable)).thenReturn(pageBooks);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        Page<BookDto> actual = bookService.findAll(pageable);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        verify(bookRepository, times(1)).findAll(pageable);
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Delete existing book")
    void deleteById_deleteExistingBook_true() {
        Long bookId = 1L;

        bookService.deleteById(bookId);

        verify(bookRepository, times(1)).deleteById(bookId);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Update exposition book")
    void updateById_correctBookUpdate_returnUpdatedBook() {
        Long bookId = 1L;
        Book book = TestDataFactory.getBook();
        BookDto expected = TestDataFactory.getBookDto();
        CreateBookRequestDto requestDto = TestDataFactory.getRequestDto();
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expected);

        BookDto actual = bookService.updateById(bookId, requestDto);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).toDto(book);
        verify(bookMapper, times(1)).updateBook(requestDto, book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Checks if the method will throw an exception when we have wrong book id")
    void updateById_updateNonExistBook_throwException() {
        Long bookId = 100L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.findById(bookId)
        );

        String expected = "Can't find book by id " + bookId;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Find all books by search parameters")
    void search_searchBooksBySearchParameter_returnCorrectListOfBook() {
        BookSearchParametersDto params = TestDataFactory.getSearchParameters();
        Specification<Book> spec = (root, query, cb) -> cb.conjunction();
        Book book = TestDataFactory.getBook();
        BookDto bookDto = TestDataFactory.getBookDto();
        when(specificationBuilder.build(params)).thenReturn(spec);
        when(bookRepository.findAll(spec)).thenReturn(List.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        List<BookDto> actual = bookService.search(params);

        assertThat(actual.get(0)).usingRecursiveComparison().isEqualTo(bookDto);
        assertThat(actual).hasSize(1).containsExactly(bookDto);
        verify(specificationBuilder, times(1)).build(params);
        verify(bookRepository, times(1)).findAll(spec);
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(specificationBuilder, bookRepository, bookMapper);
    }

    @Test
    @DisplayName("checks if the method will return an empty List when we have bead parameters")
    void search_noBooksFoundForSearchParameter_returnEmptyList() {
        BookSearchParametersDto params = TestDataFactory.getBeadSearchParameters();
        Specification<Book> spec = (root, query, cb) -> cb.conjunction();
        when(specificationBuilder.build(params)).thenReturn(spec);
        when(bookRepository.findAll(spec)).thenReturn(Collections.emptyList());

        List<BookDto> actual = bookService.search(params);

        assertThat(actual).isEmpty();
        verify(specificationBuilder, times(1)).build(params);
        verify(bookRepository, times(1)).findAll(spec);
        verifyNoMoreInteractions(specificationBuilder, bookRepository);
    }

    @Test
    @DisplayName("Find all books by category id")
    void findAllByCategoryId_findBooksByCorrectCategory_returnCorrectListOfBooks() {
        Long categoryId = 1L;
        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds = TestDataFactory
                .getBookWithoutCategory();
        when(bookRepository.findAllByCategories_Id(categoryId))
                .thenReturn(List.of(bookDtoWithoutCategoryIds));
        List<BookDtoWithoutCategoryIds> expected = List.of(bookDtoWithoutCategoryIds);

        List<BookDtoWithoutCategoryIds> actual = bookService.findAllByCategoryId(categoryId);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        verify(bookRepository, times(1)).findAllByCategories_Id(categoryId);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("checks if the method will return an empty sheet when we have bead parameters")
    void findAllByCategoryId_findBooksByNonExistCategory_returnEmptyList() {
        Long categoryId = 100L;
        when(bookRepository.findAllByCategories_Id(categoryId))
                .thenReturn(Collections.emptyList());

        List<BookDtoWithoutCategoryIds> actual = bookService.findAllByCategoryId(categoryId);

        assertThat(actual).isEmpty();
        verify(bookRepository, times(1)).findAllByCategories_Id(categoryId);
        verifyNoMoreInteractions(bookRepository);
    }
}
