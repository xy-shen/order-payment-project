package com.example.order_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.order_service.entity.Order;
import com.example.order_service.repository.OrderRepo;
import com.example.order_service.util.OrderStatus;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepo orderRepo;

    @Mock
    private OrderEventProducer orderEventProducer;

    @InjectMocks
    private OrderService orderService;

    @Test
    void updateByIdSendsCancellationEventWhenOrderIsCancelled() {
        Order existingOrder = buildOrder("order-1", OrderStatus.PENDING_PAYMENT);
        Order cancelledOrder = buildOrder("order-1", OrderStatus.CANCELLED);

        when(orderRepo.findById("order-1")).thenReturn(Optional.of(existingOrder));
        when(orderRepo.updateById("order-1", OrderStatus.CANCELLED)).thenReturn(cancelledOrder);

        Order result = orderService.updateById("order-1", OrderStatus.CANCELLED);

        assertEquals(OrderStatus.CANCELLED, result.getStatus());
        verify(orderRepo).updateById("order-1", OrderStatus.CANCELLED);
        verify(orderEventProducer).sendOrderCancelledEvent(cancelledOrder);
    }

    @Test
    void updateByIdDoesNotPublishEventWhenStatusDoesNotChange() {
        Order existingOrder = buildOrder("order-1", OrderStatus.CANCELLED);

        when(orderRepo.findById("order-1")).thenReturn(Optional.of(existingOrder));

        Order result = orderService.updateById("order-1", OrderStatus.CANCELLED);

        assertEquals(OrderStatus.CANCELLED, result.getStatus());
        verify(orderRepo, never()).updateById("order-1", OrderStatus.CANCELLED);
        verify(orderEventProducer, never()).sendOrderCancelledEvent(existingOrder);
    }

    @Test
    void updateByIdDoesNotPublishCancellationEventForOtherStatuses() {
        Order existingOrder = buildOrder("order-1", OrderStatus.PENDING_PAYMENT);
        Order confirmedOrder = buildOrder("order-1", OrderStatus.CONFIRMED);

        when(orderRepo.findById("order-1")).thenReturn(Optional.of(existingOrder));
        when(orderRepo.updateById("order-1", OrderStatus.CONFIRMED)).thenReturn(confirmedOrder);

        Order result = orderService.updateById("order-1", OrderStatus.CONFIRMED);

        assertEquals(OrderStatus.CONFIRMED, result.getStatus());
        verify(orderRepo).updateById("order-1", OrderStatus.CONFIRMED);
        verify(orderEventProducer, never()).sendOrderCancelledEvent(confirmedOrder);
    }

    private Order buildOrder(String id, OrderStatus status) {
        Order order = new Order();
        order.setId(id);
        order.setStatus(status);
        order.setAmount(100);
        order.setCreatedAt(LocalDateTime.now().minusMinutes(1));
        order.setUpdatedAt(LocalDateTime.now());
        return order;
    }
}
