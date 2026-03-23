package com.ankush.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ankush.model.BookRequest;
import com.ankush.model.RequestStatus;
import com.ankush.service.BookRequestService;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin
public class BookRequestController {

    @Autowired
    private BookRequestService requestService;

    // USER REQUEST BOOK
    @PostMapping
    public BookRequest createRequest(
            @RequestParam String bookTitle,
            @RequestParam String author,
            @RequestParam(required = false) String description,
            @RequestParam String email) {

        return requestService.createRequest(bookTitle, author, description, email);
    }

    // LIBRARIAN VIEW ALL REQUESTS
    @GetMapping
    public List<BookRequest> getAllRequests() {
        return requestService.getAllRequests();
    }

    // LIBRARIAN APPROVE / REJECT
    @PatchMapping("/{id}")
    public BookRequest updateStatus(@PathVariable Long id,
                                    @RequestParam RequestStatus status) {
        return requestService.updateStatus(id, status);
    }
   
}