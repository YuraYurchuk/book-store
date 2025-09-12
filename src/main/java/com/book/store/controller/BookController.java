package com.book.store.controller;

import com.book.store.dto.book.BookDto;
import com.book.store.dto.book.BookDtoWithoutCategoryIds;
import com.book.store.dto.book.BookSearchParametersDto;
import com.book.store.dto.book.CreateBookRequestDto;
import com.book.store.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Books", description = "Operations related to managing books")
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @Operation(
            summary = "Save new Book",
            description = "Creates and saves a new book in the database"
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public BookDto save(@RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookService.save(requestDto);
    }

    @Operation(
            summary = "Get all books",
            description = "Returns a paginated list of all books with optional sorting"
    )
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public Page<BookDtoWithoutCategoryIds> findAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @Operation(
            summary = "Get book by ID",
            description = "Returns a single book by its ID"
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public BookDto getById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @Operation(
            summary = "Delete book by ID",
            description = "Deletes a book from the database by its ID"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteById(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @Operation(
            summary = "Update book by ID",
            description = "Updates an existing book with new data"
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public BookDto updateById(@PathVariable Long id,
                              @RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookService.updateById(id, requestDto);
    }

    @Operation(
            summary = "Search books",
            description = "Searches for books by title, author, or ISBN"
    )
    @GetMapping("/search")
    @PreAuthorize("hasRole('USER')")
    public List<BookDto> search(BookSearchParametersDto bookSearchParametersDto) {
        return bookService.search(bookSearchParametersDto);
    }
}
