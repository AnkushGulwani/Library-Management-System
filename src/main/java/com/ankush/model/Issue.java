package com.ankush.model;
 
import jakarta.persistence.*;
import java.time.LocalDate;
 
@Entity
@Table(name = "issues")
public class Issue {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
 
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
 
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
 
    private Double fine;
    private boolean returned;
 
    // NEW — set to true when the user pays the fine via the payment system
    private boolean finePaid = false;
 
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
 
    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }
 
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
 
    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }
 
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
 
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
 
    public Double getFine() { return fine; }
    public void setFine(Double fine) { this.fine = fine; }
 
    public boolean isReturned() { return returned; }
    public void setReturned(boolean returned) { this.returned = returned; }
 
    public boolean isFinePaid() { return finePaid; }
    public void setFinePaid(boolean finePaid) { this.finePaid = finePaid; }
}