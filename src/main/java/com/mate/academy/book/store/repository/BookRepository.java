package com.mate.academy.book.store.repository;

import com.mate.academy.book.store.model.Book;
import java.util.List;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
