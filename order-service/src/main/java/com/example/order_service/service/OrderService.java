package com.example.order_service.service;

import com.example.order_service.dto.CreateOrderRequest;
import com.example.order_service.entity.Order;
import com.example.order_service.repository.OrderRepo;
import com.example.order_service.util.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepo orderRepo;
    private final OrderEventProducer orderEventProducer;

    public Order create(CreateOrderRequest request) {
        Order order = new Order();
        order.setAmount(request.getAmount());
        order.setStatus(OrderStatus.PENDING_PAYMENT);

        LocalDateTime now = LocalDateTime.now();
        order.setCreatedAt(now);
        order.setUpdatedAt(now);

        order.setId(null);

        Order saved = orderRepo.create(order);

        orderEventProducer.sendOrderCreatedEvent(saved);
        return saved;
    }

    public Order findById(String id) {
        return orderRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Not found"));
    }


    public List<Order> findAll() {
        return orderRepo.findAll();
    }

    public Order updateById(String id, OrderStatus status) {
        Order existingOrder = findById(id);
        // not changed
        if (existingOrder.getStatus() == status) {
            return existingOrder;
        }

        Order updatedOrder = orderRepo.updateById(id, status);
        if (updatedOrder == null) {
            throw new RuntimeException("Not found");
        }

        // cancel payment
        if (status == OrderStatus.CANCELLED) {
            orderEventProducer.sendOrderCancelledEvent(updatedOrder);
        }

        return updatedOrder;
    }
}
