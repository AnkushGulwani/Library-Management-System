package com.ankush.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ankush.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
	Optional<Book> findByTitleIgnoreCase(String title);
	boolean existsByTitleIgnoreCaseAndAuthorIgnoreCase(String title, String author);
}