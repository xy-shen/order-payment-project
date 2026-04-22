package com.example.payment_service.service;

import com.example.payment_service.util.PaymentStatus;
import com.example.payment_service.entity.Payment;
import com.example.payment_service.exception.PaymentNotFoundException;
import com.example.payment_service.repository.PaymentRepo;
import events.OrderCreatedEvent;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentService {

    private final PaymentRepo paymentRepo;
    private final PaymentEventProducer paymentEventProducer;

    public Payment findById(Long id) {
        return paymentRepo.findById(id)
            .orElseThrow(() -> new PaymentNotFoundException(id));
    }

    public List<Payment> findAll() {
        return paymentRepo.findAll();
    }

    public Payment updateById(Long id, PaymentStatus status) {
        Payment payment = findById(id);
        return updatePaymentStatus(payment, status, true);
    }

    public Payment cancelByOrderId(String orderId) {
        Payment payment = paymentRepo.findByOrderId(orderId)
            .orElseThrow(() -> new PaymentNotFoundException(orderId));

        return updatePaymentStatus(payment, PaymentStatus.CANCELLED, false);
    }

    private Payment updatePaymentStatus(Payment payment, PaymentStatus status, boolean publishEvent) {
        if (payment.getStatus() == status) {
            return payment;
        }

        payment.setStatus(status);
        Payment updatedPayment = paymentRepo.save(payment);

        if (publishEvent) {
            paymentEventProducer.sendPaymentCompletedEvent(updatedPayment);
        }

        return updatedPayment;
    }

    public boolean alreadyProcessed(String eventId) {
        return paymentRepo.findByEventId(eventId).isPresent();
    }

    public Payment createFromEvent(OrderCreatedEvent event) {

        Payment payment = new Payment();
        payment.setOrderId(event.getOrderId());
        payment.setAmount(event.getAmount());
        payment.setEventId(event.getEventId());
        payment.setStatus(PaymentStatus.PENDING);

        return paymentRepo.save(payment);
    }
}
