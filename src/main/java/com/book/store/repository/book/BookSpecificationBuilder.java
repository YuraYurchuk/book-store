package com.book.store.repository.book;

import com.book.store.dto.BookSearchParametersDto;
import com.book.store.model.Book;
import com.book.store.repository.SpecificationBuilder;
import com.book.store.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private static final String TITLE = "title";
    private static final String AUTHOR = "author";
    private static final String ISBN = "isbn";
    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParametersDto) {
        Specification<Book> spec = Specification.where(null);
        if (searchParametersDto.title() != null && searchParametersDto.title().length > 0) {
            spec = spec.and(specificationProviderManager.getSpecificationProvider(TITLE)
                    .getSpecification(searchParametersDto.title()));
        }
        if (searchParametersDto.author() != null && searchParametersDto.author().length > 0) {
            spec = spec.and(specificationProviderManager.getSpecificationProvider(AUTHOR)
                    .getSpecification(searchParametersDto.author()));
        }
        if (searchParametersDto.isbn() != null && searchParametersDto.isbn().length > 0) {
            spec = spec.and(specificationProviderManager.getSpecificationProvider(ISBN)
                    .getSpecification(searchParametersDto.isbn()));
        }
        return spec;
    }
}
