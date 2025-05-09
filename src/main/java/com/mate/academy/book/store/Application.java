package com.mate.academy.book.store;

import com.mate.academy.book.store.model.Book;
import com.mate.academy.book.store.service.BookService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Book hyperion = new Book();
                hyperion.setTitle("Hyperion");
                hyperion.setAuthor("Den Simons");
                hyperion.setIsbn("978-966-10-4643-5");
                hyperion.setPrice(BigDecimal.valueOf(500));

                bookService.save(hyperion);
                System.out.println(bookService.findAll());
            }
        };
    }

}
