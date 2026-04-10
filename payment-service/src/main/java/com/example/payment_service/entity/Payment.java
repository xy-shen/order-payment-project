package com.example.payment_service.entity;

import com.example.payment_service.PaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Payment {
    @Id
    Long id;

    Long orderId; // foreign key?

    int amount;

    PaymentStatus status;
}
