package com.example.Books.repository;

import com.example.Books.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository("bookRepository")
public interface BookRepository extends JpaRepository<Book,Long> {

}
