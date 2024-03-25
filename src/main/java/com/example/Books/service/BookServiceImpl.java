package com.example.Books.service;

import com.example.Books.dto.BookDTO;
import com.example.Books.model.Book;
import com.example.Books.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService{

    private BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    @Override
    public List<BookDTO> findAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream().map(this::mapToBookDTO).collect(Collectors.toList()).reversed();
    }

    public Optional<Book> findBook(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException(
                        String.format("Book with Id %s does not exist in the repository", id)
                )
        );
        bookRepository.delete(book);
    }

    public Book saveBook(Book bookDTO) {
        Book book = new Book();
        book.setId(bookDTO.getId());
        book.setAuthor(bookDTO.getAuthor());
        book.setBookName(bookDTO.getBookName());

        return bookRepository.save(book);
    }

    public Book updateBook(Long id, String author, String bookName) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + id));
        book.setAuthor(author);
        book.setBookName(bookName);

        return bookRepository.save(book);
    }

    private BookDTO mapToBookDTO(Book book){
        return BookDTO.builder()
                .id(book.getId())
                .author(book.getAuthor())
                .bookName(book.getBookName())
                .build();
    }
}