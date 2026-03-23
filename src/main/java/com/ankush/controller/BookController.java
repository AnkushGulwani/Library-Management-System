package com.ankush.controller;

import com.ankush.model.Book;
import com.ankush.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin
public class BookController {

    @Autowired
    private BookService bookService;

    private final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    // ✅ ADD BOOK
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Book addBook(
            @RequestParam String title,
            @RequestParam String author,
            @RequestParam String isbn,
            @RequestParam Integer totalCopies,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Double mrp,
            @RequestParam(required = false) MultipartFile cover
    ) throws IOException {

        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        book.setTotalCopies(totalCopies);
        book.setGenre(genre);
        book.setMrp(mrp);

        // DEFAULT IMAGE
        if (cover == null || cover.isEmpty()) {
            book.setCoverImage("default.png");
        } else {
            String fileName = System.currentTimeMillis() + "_" + cover.getOriginalFilename();
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            cover.transferTo(new File(UPLOAD_DIR + fileName));
            book.setCoverImage(fileName);
        }

        return bookService.addBook(book);
    }

    // ✅ UPDATE BOOK (WITH OPTIONAL IMAGE CHANGE)
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Book updateBook(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam String author,
            @RequestParam String isbn,
            @RequestParam Integer totalCopies,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Double mrp,
            @RequestParam(required = false) MultipartFile cover
    ) throws IOException {

        Book book = bookService.getBookById(id);

        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        book.setTotalCopies(totalCopies);
        book.setGenre(genre);
        book.setMrp(mrp);

        // If new image uploaded
        if (cover != null && !cover.isEmpty()) {

            // Delete old image if not default
            if (book.getCoverImage() != null && !book.getCoverImage().equals("default.png")) {
                File oldFile = new File(UPLOAD_DIR + book.getCoverImage());
                if (oldFile.exists()) oldFile.delete();
            }

            String fileName = System.currentTimeMillis() + "_" + cover.getOriginalFilename();
            cover.transferTo(new File(UPLOAD_DIR + fileName));
            book.setCoverImage(fileName);
        }

        return bookService.updateBookEntity(book);
    }

    // GET ALL BOOKS
    @GetMapping
    public List<Book> getBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Boolean available
    ) {
        return bookService.searchBooks(title, author, genre, available);
    }

    // DELETE BOOK + IMAGE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {

        Book book = bookService.getBookById(id);

        // Delete image file
        if (book.getCoverImage() != null && !book.getCoverImage().equals("default.png")) {
            File file = new File(UPLOAD_DIR + book.getCoverImage());
            if (file.exists()) file.delete();
        }

        bookService.deleteBook(id);
        return ResponseEntity.ok("Book deleted successfully");
    }
}