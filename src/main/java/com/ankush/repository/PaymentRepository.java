package com.ankush.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ankush.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}