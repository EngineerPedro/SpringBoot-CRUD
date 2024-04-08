package com.example.Books.maps;

import com.example.Books.dto.BookDTO;
import com.example.Books.model.Book;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookDTOConverter {

    @Autowired
    private ModelMapper modelMapper;
    public BookDTO covertBooktoBookDTO(Book book){
        BookDTO bookDTO = modelMapper.map(book, BookDTO.class);
        return bookDTO;
    }

    public Book covertBookDTOtoBook(BookDTO bookDTO){
        Book book  = modelMapper.map(bookDTO, Book.class);
        return book;
    }
}
