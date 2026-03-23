package com.ankush.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.ankush.model.*;
import com.ankush.repository.*;

@Service
public class IssueService {

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    // ==============================
    // EMAIL SENDER METHOD
    // ==============================
    private void sendEmail(String to, String subject, String body) {

        try {

            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom("ankushgulwani18@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);

        } catch (Exception e) {

            System.out.println("Email sending failed: " + e.getMessage());

        }
    }

    // ==============================
    // ISSUE BOOK
    // ==============================
    public Issue issueBook(String bookTitle, String userEmail, String dueDateStr) {

        Book book = bookRepository.findByTitleIgnoreCase(bookTitle)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        User user = userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (book.getAvailableCopies() <= 0) {
            throw new RuntimeException("Book not available");
        }

        // reduce available copies
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        Issue issue = new Issue();

        issue.setBook(book);
        issue.setUser(user);
        issue.setIssueDate(LocalDate.now());

        if (dueDateStr != null && !dueDateStr.isEmpty()) {
            issue.setDueDate(LocalDate.parse(dueDateStr));
        } else {
            issue.setDueDate(LocalDate.now().plusDays(14));
        }

        issue.setReturned(false);
        issue.setFine(0.0);

        Issue savedIssue = issueRepository.save(issue);

        // 🔔 In-app notification
        notificationService.createNotification(
                user.getId(),
                "Book Issued",
                "The book \"" + book.getTitle() +
                        "\" has been issued to you. Due date: " + issue.getDueDate()
        );

        // 📧 Email notification
        sendEmail(
                user.getEmail(),
                "Book Issued Successfully",
                "Hello " + user.getFirstName() +
                        "\n\nThe book \"" + book.getTitle() + "\" has been issued to you." +
                        "\nDue Date: " + issue.getDueDate() +
                        "\n\nPlease return the book before due date to avoid fines."
        );

        return savedIssue;
    }

    // ==============================
    // RETURN BOOK
    // ==============================
    public Issue returnBook(Long issueId, Boolean damaged) {

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new RuntimeException("Issue not found"));

        if (issue.isReturned()) {
            throw new RuntimeException("Book already returned");
        }

        issue.setReturnDate(LocalDate.now());
        issue.setReturned(true);

        Book book = issue.getBook();

        double fine = 0;

        // ==============================
        // DAMAGE FINE
        // ==============================
        if (damaged != null && damaged) {

            fine = book.getMrp();

            notificationService.createNotification(
                    issue.getUser().getId(),
                    "Book Damaged",
                    "You returned damaged book \"" + book.getTitle() +
                            "\". Fine applied: ₹" + fine
            );
        }

        // ==============================
        // LATE RETURN FINE
        // ==============================
        else {

            long overdueDays = ChronoUnit.DAYS.between(issue.getDueDate(), LocalDate.now());

            if (overdueDays > 0) {

                fine = overdueDays * 10;

                notificationService.createNotification(
                        issue.getUser().getId(),
                        "Late Return",
                        "Book \"" + book.getTitle() +
                                "\" returned late. Fine applied: ₹" + fine
                );
            }
        }

        issue.setFine(fine);

        // ==============================
        // NORMAL RETURN NOTIFICATION
        // ==============================
        if (fine == 0) {

            notificationService.createNotification(
                    issue.getUser().getId(),
                    "Book Returned",
                    "You successfully returned \"" + book.getTitle() + "\"."
            );
        }

        // update available copies
        book.setAvailableCopies(book.getAvailableCopies() + 1);

        if (book.getAvailableCopies() == book.getTotalCopies()) {
            book.setStatus(BookStatus.AVAILABLE);
        } else {
            book.setStatus(BookStatus.PARTIAL);
        }

        bookRepository.save(book);

        // ==============================
        // EMAIL FOR RETURN
        // ==============================
        String emailBody = "Hello " + issue.getUser().getFirstName() +
                "\n\nYou returned the book \"" + book.getTitle() + "\".";

        if (fine > 0) {
            emailBody += "\nFine Applied: ₹" + fine;
        }

        emailBody += "\n\nLibrary Management System";

        sendEmail(
                issue.getUser().getEmail(),
                "Book Return Update",
                emailBody
        );

        return issueRepository.save(issue);
    }

    // ==============================
    // FETCH ALL ISSUES
    // ==============================
    public List<Issue> getAllIssues() {
        return issueRepository.findAll();
    }

    // ==============================
    // MEMBER HISTORY
    // ==============================
    public List<Issue> getMemberHistory(String userName) {

        User user = userRepository.findByFirstNameIgnoreCase(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return issueRepository.findByUser(user);
    }

    // ==============================
    // CURRENTLY ISSUED BOOKS
    // ==============================
    public List<Issue> getIssuedBooks(Long userId) {
        return issueRepository.findByUser_IdAndReturned(userId, false);
    }

    // ==============================
    // READING HISTORY
    // ==============================
    public List<Issue> getReadingHistory(Long userId) {
        return issueRepository.findByUser_IdAndReturned(userId, true);
    }

    // ==============================
    // PENDING FINES
    // ==============================
    public List<Issue> getPendingFines(Long userId) {
        return issueRepository.findByUser_IdAndFineGreaterThan(userId, 0);
    }

    // ==============================
    // TOTAL FINE
    // ==============================
    public double getTotalFine(Long userId) {

        return getPendingFines(userId)
                .stream()
                .mapToDouble(issue -> issue.getFine() != null ? issue.getFine() : 0)
                .sum();
    }
}