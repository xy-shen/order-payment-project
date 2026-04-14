package com.example.order_service.service;

import com.example.order_service.dto.CreateOrderRequest;
import com.example.order_service.entity.Order;
import com.example.order_service.repository.OrderRepo;
import com.example.order_service.util.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepo orderRepo;

    public Order create(CreateOrderRequest request) {
        Order order = new Order();
        order.setAmount(request.getAmount());
        order.setStatus(OrderStatus.PENDING_PAYMENT);

        LocalDateTime now = LocalDateTime.now();
        order.setCreatedAt(now);
        order.setUpdatedAt(now);

        order.setId(null);

        return orderRepo.create(order);
    }

    public Order findById(String id) {
        return orderRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Not found"));
    }


    public List<Order> findAll() {
        return orderRepo.findAll();
    }

    public Order updateById(String id, OrderStatus status) {
        return orderRepo.updateById(id, status);
    }
}
