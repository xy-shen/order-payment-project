package com.example.payment_service.service;

import com.example.payment_service.PaymentStatus;
import com.example.payment_service.entity.Payment;
import com.example.payment_service.repository.PaymentRepo;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentService {
    private final PaymentRepo paymentRepo;

    public Payment create(Payment payment) {
        return paymentRepo.save(payment);
    }

    public Payment findById(Long id) {
        return paymentRepo.findById(id);
    }

    public List<Payment> findAll() {
        return paymentRepo.findAll();
    }

    public Payment updateById(Long id, PaymentStatus status) {
        return paymentRepo.updateById(id, status);
    }
}
