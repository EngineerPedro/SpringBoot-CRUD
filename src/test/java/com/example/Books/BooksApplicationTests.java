package com.example.Books;

import com.example.Books.dto.BookDTO;
import com.example.Books.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BooksApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
	void contextLoads() {
	}

    @Test
    void findAllBooksTest() {
        BookDTO book1 = BookDTO.builder().bookName("Book 1").author("Author 1").build();
        BookDTO book2 = BookDTO.builder().bookName("Book 2").author("Author 2").build();

        when(bookService.findAllBooks()).thenReturn(Arrays.asList(book1, book2));

        List<BookDTO> result = bookService.findAllBooks();

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(bookService, times(1)).findAllBooks();
    }
}
