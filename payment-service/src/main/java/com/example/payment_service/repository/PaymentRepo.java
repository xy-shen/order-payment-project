package com.example.payment_service.repository;

import com.example.payment_service.PaymentStatus;
import com.example.payment_service.entity.Payment;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepo {
    Payment save(Payment payment);
    Payment findById(Long id);
    List<Payment> findAll();
    Payment updateById(Long id, PaymentStatus status);
}
