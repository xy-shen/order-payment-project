package com.example.order_service.service;

import com.example.order_service.util.OrderStatus;
import events.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentResultConsumer {

    private final OrderService orderService;

    @KafkaListener(topics = "payment.completed", groupId = "order-group")
    public void consume(PaymentCompletedEvent event) {
        orderService.updateById(
            event.getOrderId(),
            mapStatus(event.getStatus())
        );
    }

    private OrderStatus mapStatus(String status) {
        if ("SUCCESS".equals(status)) {
            return OrderStatus.CONFIRMED;
        }

        if ("CANCELLED".equals(status)) {
            return OrderStatus.CANCELLED;
        }

        return OrderStatus.PENDING_PAYMENT;
    }
}
