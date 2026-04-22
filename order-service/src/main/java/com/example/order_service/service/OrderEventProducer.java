package com.example.order_service.service;

import com.example.order_service.entity.Order;
import events.OrderCancelledEvent;
import events.OrderCreatedEvent;
import java.util.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderEventProducer {
    @Autowired
    private final KafkaTemplate<String, Object> kafkaTemplate;

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

    public void sendOrderCancelledEvent(Order order) {
        OrderCancelledEvent event = new OrderCancelledEvent(
            UUID.randomUUID().toString(),
            order.getId(),
            order.getUpdatedAt()
        );

        kafkaTemplate.send(
            "order.cancelled",
            order.getId(),
            event
        );
    }
}
