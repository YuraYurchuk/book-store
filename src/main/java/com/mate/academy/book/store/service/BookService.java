package com.mate.academy.book.store.service;

import com.mate.academy.book.store.model.Book;
import java.util.List;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
