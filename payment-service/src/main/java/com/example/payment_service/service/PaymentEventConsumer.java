package com.example.payment_service.service;

import com.example.payment_service.entity.Payment;
import events.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentEventConsumer {

    private final PaymentService paymentService;
    private final PaymentEventProducer paymentEventProducer;

    @KafkaListener(topics = "order.created", groupId = "payment-group")
    public void consume(OrderCreatedEvent event) {

        // 🔥 Idempotency check (simplified)
        if (paymentService.alreadyProcessed(event.getEventId())) {
            return;
        }

        Payment payment = paymentService.createFromEvent(event);

        // publish result
        paymentEventProducer.sendPaymentCompletedEvent(payment);
    }
}
