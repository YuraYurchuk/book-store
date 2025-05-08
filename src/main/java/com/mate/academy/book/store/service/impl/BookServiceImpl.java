package com.mate.academy.book.store.service.impl;

import com.mate.academy.book.store.model.Book;
import com.mate.academy.book.store.repository.BookRepository;
import com.mate.academy.book.store.service.BookService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }
}
