package com.example.order_service.service;

import com.example.order_service.entity.Order;
import events.OrderCreatedEvent;
import java.util.*;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderEventProducer {
    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public void sendOrderCreatedEvent(Order order) {
        OrderCreatedEvent event = new OrderCreatedEvent(
            UUID.randomUUID().toString(),
            order.getId(),
            order.getAmount(),
            order.getCreatedAt()
        );

        kafkaTemplate.send(
            "order.created",
            order.getId(),   // 🔥 key for ordering
            event
        );
    }
}
