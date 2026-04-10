package com.example.order_service.service;

import com.example.order_service.entity.Order;
import com.example.order_service.repository.OrderRepo;
import com.example.order_service.util.OrderStatus;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepo orderRepo;

    public Order create(Order order) {
        return orderRepo.create(order);
    }

    public Order findById(String id) {
        return orderRepo.findById(id);
    }


    public List<Order> findAll() {
        return orderRepo.findAll();
    }

    public Order updateById(String id, OrderStatus status) {
        return orderRepo.updateById(id, status);
    }
}
