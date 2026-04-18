package com.example.payment_service.service;

import com.example.payment_service.util.PaymentStatus;
import com.example.payment_service.entity.Payment;
import com.example.payment_service.repository.PaymentRepo;
import events.OrderCreatedEvent;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentService {

    private final PaymentRepo paymentRepo;

//    public Payment create(Payment payment) {
//        return paymentRepo.save(payment);
//    }

    public Payment findById(Long id) {
        return paymentRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Not found"));
    }

    public List<Payment> findAll() {
        return paymentRepo.findAll();
    }

    public Payment updateById(Long id, PaymentStatus status) {
        Payment payment = paymentRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Payment Not found"));

        payment.setStatus(status);
        return paymentRepo.save(payment);
    }

    public boolean alreadyProcessed(String eventId) {
        return paymentRepo.findByEventId(eventId).isPresent();
    }

    public Payment createFromEvent(OrderCreatedEvent event) {

        Payment payment = new Payment();
        payment.setOrderId(event.getOrderId());
        payment.setAmount(event.getAmount());
        payment.setEventId(event.getEventId());
        payment.setStatus(PaymentStatus.SUCCESS);

        return paymentRepo.save(payment);
    }
}
