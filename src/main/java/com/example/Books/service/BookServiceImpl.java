package com.example.Books.service;

import com.example.Books.maps.BookDTOConverter;
import com.example.Books.dto.BookDTO;
import com.example.Books.model.Book;
import com.example.Books.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService{

    private BookRepository bookRepository;

    @Autowired
    BookDTOConverter bookDTOConverter;

    public BookServiceImpl(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }



    @Override
    public List<BookDTO> findAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(bookDTOConverter::covertBooktoBookDTO)
                .collect(Collectors.toList());
    }

    public Optional<Book> findBook(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public BookDTO retrieveBookById(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            return  bookDTOConverter.covertBooktoBookDTO(book.get());
        } else {
            throw new ResourceNotFoundException("Book not found");
        }
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

    public Book createNewBook(Book bookInfo) {
        Book book = new Book();
        book.setId(bookInfo.getId());
        book.setAuthor(bookInfo.getAuthor());
        book.setBookName(bookInfo.getBookName());

        return bookRepository.save(book);
    }

    public Book updateBook(Long id, String author, String bookName) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + id));
        book.setAuthor(author);
        book.setBookName(bookName);

        return bookRepository.save(book);
    }


}