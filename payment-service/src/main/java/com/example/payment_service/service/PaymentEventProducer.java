package com.example.payment_service.service;

import com.example.payment_service.entity.Payment;
import events.PaymentCompletedEvent;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentEventProducer {

    private final KafkaTemplate<String, PaymentCompletedEvent> kafkaTemplate;

    public void sendPaymentCompletedEvent(Payment payment) {
        PaymentCompletedEvent event = new PaymentCompletedEvent(
            UUID.randomUUID().toString(),
            payment.getOrderId(),
            payment.getStatus().name()
        );

        kafkaTemplate.send(
            "payment.completed",
            payment.getOrderId(),
            event
        );
    }
}
