package com.example.payment_service.controller;

import com.example.payment_service.PaymentStatus;
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

    @PostMapping
    public Payment create(@RequestBody Payment payment) {
        return paymentService.create(payment);
    }

    @GetMapping("/{id}")
    public Payment findById(@RequestParam Long id) {
        return paymentService.findById(id);
    }

    @GetMapping
    public List<Payment> findAll() {
        return paymentService.findAll();
    }

    @PatchMapping("/{id}")
    public Payment updateById(@RequestParam Long id, @PathVariable PaymentStatus status) {
        return paymentService.updateById(id, status);
    }
}
