package com.example.Books.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookDTO {
    private Long id;

    private String bookName;

    private String author;
}
