package com.ankush.service;
 
import com.ankush.model.Payment;
import com.ankush.repository.IssueRepository;
import com.ankush.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import java.time.LocalDateTime;
 
@Service
public class PaymentService {
 
    @Autowired
    private PaymentRepository paymentRepository;
 
    @Autowired
    private IssueRepository issueRepository;
 
    // Called without issueId (fallback — zeroes all unpaid fines for user)
    public Payment processPayment(Long userId, Double amount, String method) {
        return processPayment(userId, amount, method, null);
    }
 
    // Main overload — also accepts issueId so the fine is cleared on the exact Issue
    public Payment processPayment(Long userId, Double amount, String method, Long issueId) {
 
        Payment payment = new Payment();
        payment.setUserId(userId);
        payment.setAmount(amount);
        payment.setMethod(method);
        payment.setStatus("SUCCESS");
        payment.setPaymentDate(LocalDateTime.now());
 
        Payment saved = paymentRepository.save(payment);
 
        if (issueId != null) {
            // Clear fine and mark as paid on the specific Issue
            issueRepository.findById(issueId).ifPresent(issue -> {
                issue.setFine(0.0);
                issue.setFinePaid(true);   // ← NEW
                issueRepository.save(issue);
            });
        } else {
            // No issueId — zero out all unpaid fines for this user
            issueRepository.findAll().stream()
                .filter(i -> i.getUser() != null
                          && i.getUser().getId().equals(userId)
                          && !i.isReturned()
                          && i.getFine() != null
                          && i.getFine() > 0)
                .forEach(i -> {
                    i.setFine(0.0);
                    i.setFinePaid(true);   // ← NEW
                    issueRepository.save(i);
                });
        }
 
        return saved;
    }
}