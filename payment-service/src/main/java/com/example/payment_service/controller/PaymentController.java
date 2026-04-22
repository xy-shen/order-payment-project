package com.example.payment_service.controller;

import com.example.payment_service.util.PaymentStatus;
import com.example.payment_service.entity.Payment;
import com.example.payment_service.service.PaymentService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@AllArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/{id}")
    public Payment findById(@PathVariable Long id) {
        return paymentService.findById(id);
    }

    @GetMapping
    public List<Payment> findAll() {
        return paymentService.findAll();
    }

    @PatchMapping("/{id}")
    public Payment updateById(@PathVariable Long id, @RequestBody PaymentStatus status) {
        return paymentService.updateById(id, status);
    }
}
