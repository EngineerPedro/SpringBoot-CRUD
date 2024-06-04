package com.example.Books.controller;

import com.example.Books.dto.BookDTO;
import com.example.Books.model.Book;
import com.example.Books.service.BookService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class BookController {
    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public List<BookDTO> listAllBooks(){
        return bookService.findAllBooks();
    }

    @GetMapping("/books/{id}")
    public BookDTO getBookById(@PathVariable Long id) {

        return bookService.retrieveBookById(id);
    }

    @PostMapping("/books")
    public ResponseEntity<Object> createBook(@RequestBody Book book) {
        Book savedBook = bookService.createNewBook(book);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedBook.getId()).toUri();

        return ResponseEntity.ok().header(HttpHeaders.LOCATION, location.toString()).body(savedBook);
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<Object> updateBook(@PathVariable Long id, @RequestBody Book book) {
        Optional<Book> existingBook = bookService.findBook(id);
        if(existingBook == null) {
            return ResponseEntity.notFound().build();
        }
        book.setId(id);
        bookService.updateBook(book.getId(),book.getAuthor(),book.getBookName());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<Object> deleteBook(@PathVariable Long id) {
        Optional<Book> existingBook = bookService.findBook(id);
        if (existingBook == null) {
            return ResponseEntity.notFound().build();
        }
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}