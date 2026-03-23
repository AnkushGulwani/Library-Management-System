package com.ankush.controller;
 
import com.ankush.model.Payment;
import com.ankush.repository.PaymentRepository;
import com.ankush.service.PaymentService;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
 
@RestController
@RequestMapping("/api/payment")
@CrossOrigin
public class PaymentController {
 
    @Autowired
    private PaymentService paymentService;
 
    @Autowired
    private PaymentRepository paymentRepository;
 
    // Process a fine payment (called from user dashboard)
    @PostMapping("/pay")
    public Payment payFine(@RequestParam Long userId,
                           @RequestParam Double amount,
                           @RequestParam String method,
                           @RequestParam(required = false) Long issueId) {
 
        return paymentService.processPayment(userId, amount, method, issueId);
    }
 
    // NEW — fetch all payment records for librarian audit view
    @GetMapping("/all")
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}