package com.example.payment_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.payment_service.entity.Payment;
import com.example.payment_service.repository.PaymentRepo;
import com.example.payment_service.util.PaymentStatus;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepo paymentRepo;

    @Mock
    private PaymentEventProducer paymentEventProducer;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void updateByIdPublishesKafkaEventWhenStatusChanges() {
        Payment existingPayment = buildPayment(1L, "order-1", PaymentStatus.PENDING);
        Payment updatedPayment = buildPayment(1L, "order-1", PaymentStatus.SUCCESS);

        when(paymentRepo.findById(1L)).thenReturn(Optional.of(existingPayment));
        when(paymentRepo.save(existingPayment)).thenReturn(updatedPayment);

        Payment result = paymentService.updateById(1L, PaymentStatus.SUCCESS);

        assertEquals(PaymentStatus.SUCCESS, result.getStatus());
        verify(paymentRepo).save(existingPayment);
        verify(paymentEventProducer).sendPaymentCompletedEvent(updatedPayment);
    }

    @Test
    void updateByIdSkipsSaveAndPublishWhenStatusDoesNotChange() {
        Payment existingPayment = buildPayment(1L, "order-1", PaymentStatus.CANCELLED);

        when(paymentRepo.findById(1L)).thenReturn(Optional.of(existingPayment));

        Payment result = paymentService.updateById(1L, PaymentStatus.CANCELLED);

        assertEquals(PaymentStatus.CANCELLED, result.getStatus());
        verify(paymentRepo, never()).save(existingPayment);
        verify(paymentEventProducer, never()).sendPaymentCompletedEvent(existingPayment);
    }

    @Test
    void cancelByOrderIdCancelsPaymentWithoutPublishingEvent() {
        Payment existingPayment = buildPayment(1L, "order-1", PaymentStatus.PENDING);
        Payment cancelledPayment = buildPayment(1L, "order-1", PaymentStatus.CANCELLED);

        when(paymentRepo.findByOrderId("order-1")).thenReturn(Optional.of(existingPayment));
        when(paymentRepo.save(existingPayment)).thenReturn(cancelledPayment);

        Payment result = paymentService.cancelByOrderId("order-1");

        assertEquals(PaymentStatus.CANCELLED, result.getStatus());
        verify(paymentRepo).save(existingPayment);
        verify(paymentEventProducer, never()).sendPaymentCompletedEvent(cancelledPayment);
    }

    private Payment buildPayment(Long id, String orderId, PaymentStatus status) {
        Payment payment = new Payment();
        payment.setId(id);
        payment.setOrderId(orderId);
        payment.setAmount(100);
        payment.setEventId("event-1");
        payment.setStatus(status);
        return payment;
    }
}
