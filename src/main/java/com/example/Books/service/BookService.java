package com.example.Books.service;

import com.example.Books.dto.BookDTO;
import com.example.Books.model.Book;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BookService {

    public BookDTO getAllBooks();

    List<BookDTO> findAllBooks();

    Book saveBook(Book book);

    Book updateBook(Long id, String author, String bookName);

    Optional<Book> findBook(Long id);

    void deleteBook(Long id);
}