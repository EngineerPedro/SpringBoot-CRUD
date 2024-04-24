package com.example.Books;

import com.example.Books.dto.BookDTO;
import com.example.Books.model.Book;
import com.example.Books.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.awaitility.Awaitility.given;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest( classes = BooksApplication.class)
@AutoConfigureMockMvc
class BooksApplicationTests {

    // Autowire the MockMvc which is used to make HTTP requests
    @Autowired
    private MockMvc mockMvc;

    // Mock the BookService, pretend as it is the actual service
    @MockBean
    private BookService bookService;

    // ObjectMapper is used to convert Java objects to JSON or vice versa
    @Autowired
    private ObjectMapper objectMapper;

    // Instance variables to be used in the tests
    private BookDTO book1;
    private BookDTO book2;
    private Book book;

    // Initialize the instance variables before each test
    @BeforeEach
    void setUp() {
         book1 = BookDTO.builder()
                .author("Author 1")
                .bookName("Book 1")
                .build();

         book2 = BookDTO.builder()
                .author("Author 2")
                .bookName("Book 2")
                .build();

        book = new Book();
        book.setId(1L);
        book.setBookName("newBook");
        book.setAuthor("newAuthor");

    }



    @Test // This is a test method to validate book creation via MVC Mock.
    void createNewBookThroughMvcTest() throws Exception {

        // Mocking the service call for book creation, it will return 'book' for any book passed to the service
        BDDMockito.given(bookService.createNewBook(any(Book.class))).willReturn(book);

        // Converting the 'book' object to JSON using ObjectMapper
        String jsonBook = objectMapper.writeValueAsString(book);

        // Making a POST request to "/api/book" endpoint with 'book' in body converted to JSON
        // It expects the MediaType as APPLICATION_JSON, so we set it as ContentType
        MvcResult mvcResult = mockMvc
                .perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBook))

                // We then expect the status to be FOUND (200)
                .andExpect(status().isOk())

                // Getting the result of performed operation and storing in 'mvcResult', which might be used for further assertions
                .andReturn();
    }

     @Test // This is a test method to validate finding all books service .
     void findAllBooksThroughMvcTest () throws Exception {
            // Mock the expectations
            BDDMockito.given(bookService.findAllBooks()).willReturn(Arrays.asList(book1, book2));

            // Perform a GET request to "/books" endpoint
            MvcResult mvcResult = mockMvc.perform(get("/books")
                            .contentType(MediaType.APPLICATION_JSON))
                    // Expecting the status to be OK (200)
                    .andExpect(status().isOk())
                    // Expecting the content type to be JSON
                    .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    // Expecting the returned BookDTO list to have the same size as our list
                    .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                    // Return the result of performed operation for further assertions
                    .andReturn();

            // Convert the MvcResult's response to a BookDTO list
            List<BookDTO> actualBookDtoList = Arrays.asList(objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookDTO[].class));

            // Assert that the actual book DTO list is the same as the expected
            assertEquals(Arrays.asList(book1, book2), actualBookDtoList);
     }

    @Test // This is a test method for finding a book by its ID via MVC Mock.
    void findBookByIdThroughMvcTest() throws Exception {

        // Mock the expectations for the Book Service
        when(bookService.findBook(anyLong())).thenReturn(Optional.of(book));

        // Performing GET request to "/books/{id}" endpoint
        mockMvc.perform(get("/books/{id}", book.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                    // Expecting the status to be OK (200)
                    .andExpect(status().isOk())
                    // Return the result of performed operation for further assertions
                    .andReturn();
    }

    @Test // Test method for updating a book by its ID via MockMvc.
    void updateBookByIdThroughMvcTest() throws Exception {

        // This line mocks the book service call for the book update operation.
        // It conditions the service to return the 'book' object when the service is called with any long value,
        // and any two strings representing the new book name and new author name.
        when(bookService.updateBook(anyLong(), anyString(), anyString())).thenReturn(book);

        // The 'book' object is converted into a JSON string using ObjectMapper
        String jsonBook = objectMapper.writeValueAsString(book);

        // An HTTP PUT request is performed to the "/books/{id}" endpoint, where {id} is the id of the 'book'.
        // The operation of updating the book is simulated by passing 'book' in the body converted to JSON.
        // The HTTP request expects the MediaType as APPLICATION_JSON, so we set it as ContentType.
        mockMvc.perform(put("/books/{id}", book.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBook))
                // The test then expects the status to be OK (200) i.e. assert that the HTTP response is 200.
                .andExpect(status().is2xxSuccessful())
                // The result of the performed HTTP operation (PUT request) will be stored in 'result' which
                // may be used further for other assertions.
                .andReturn();
    }

    @Test // Test method for deleting a book by its ID via MockMvc.
    void deleteBookByIdThroughMvcTest() throws Exception {
        // Perform a DELETE request to "/books/{id}" endpoint with the id of the book
        mockMvc.perform(delete("/books/{id}", book.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                // expecting the status to be OK (204)
                .andExpect(status().is2xxSuccessful());
    }


}
