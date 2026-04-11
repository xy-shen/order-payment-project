package com.example.payment_service.repository;

import com.example.payment_service.util.PaymentStatus;
import com.example.payment_service.entity.Payment;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Long> {
    Payment save(Payment payment);
    Optional<Payment> findById(Long id);
    List<Payment> findAll();
}
