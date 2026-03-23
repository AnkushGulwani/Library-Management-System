package com.ankush.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.ankush.model.*;
import com.ankush.repository.BookRepository;
import com.ankush.repository.IssueRepository;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    // ADD BOOK
    public Book addBook(Book book) {

        if (book.getId() == null) {  // Only for new book
            book.setAvailableCopies(book.getTotalCopies());
        }

        updateStatus(book);

        return bookRepository.save(book);
    }
    public Book updateBookEntity(Book book) {

        if (book.getAvailableCopies() > book.getTotalCopies()) {
            book.setAvailableCopies(book.getTotalCopies());
        }

        updateStatus(book);
        return bookRepository.save(book);
    }

    // SEARCH & FILTER
    public List<Book> searchBooks(String title,
                                  String author,
                                  String genre,
                                  Boolean available) {

        List<Book> books = bookRepository.findAll();

        if (title != null && !title.isEmpty()) {
            books = books.stream()
                    .filter(b -> b.getTitle().toLowerCase().contains(title.toLowerCase()))
                    .toList();
        }

        if (author != null && !author.isEmpty()) {
            books = books.stream()
                    .filter(b -> b.getAuthor().toLowerCase().contains(author.toLowerCase()))
                    .toList();
        }

        if (genre != null && !genre.isEmpty()) {
            books = books.stream()
                    .filter(b -> b.getGenre().equalsIgnoreCase(genre))
                    .toList();
        }

        if (available != null) {
            books = books.stream()
                    .filter(b -> available ? b.getAvailableCopies() > 0
                            : b.getAvailableCopies() == 0)
                    .toList();
        }

        return books;
    }

    // GET BOOK BY ID
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    // UPDATE BOOK
    public Book updateBook(Long id, Book updatedBook) {

        Book book = getBookById(id);

        book.setTitle(updatedBook.getTitle());
        book.setAuthor(updatedBook.getAuthor());
        book.setGenre(updatedBook.getGenre());
        book.setIsbn(updatedBook.getIsbn());
        book.setMrp(updatedBook.getMrp());
        book.setTotalCopies(updatedBook.getTotalCopies());

        // adjust available copies if needed
        if (book.getAvailableCopies() > book.getTotalCopies()) {
            book.setAvailableCopies(book.getTotalCopies());
        }

        updateStatus(book);

        return bookRepository.save(book);
    }

    @Autowired
    private IssueRepository issueRepository;

    @Transactional
    public void deleteBook(Long id) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        issueRepository.deleteByBook(book);

        bookRepository.delete(book);
    }

    // PATCH AVAILABILITY
    public Book updateAvailability(Long id, Integer availableCopies) {

        Book book = getBookById(id);

        if (availableCopies > book.getTotalCopies()) {
            throw new RuntimeException("Available copies cannot exceed total copies");
        }

        book.setAvailableCopies(availableCopies);
        updateStatus(book);

        return bookRepository.save(book);
    }

    // AUTO STATUS UPDATE
    private void updateStatus(Book book) {

        if (book.getAvailableCopies() == 0) {
            book.setStatus(BookStatus.ISSUED);
        } else if (book.getAvailableCopies().equals(book.getTotalCopies())) {
            book.setStatus(BookStatus.AVAILABLE);
        } else {
            book.setStatus(BookStatus.PARTIAL);
        }
    }
}