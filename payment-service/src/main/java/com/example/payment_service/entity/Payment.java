package com.example.payment_service.entity;

import com.example.payment_service.util.PaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "payment_db",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_payment_event_id", columnNames = "event_id")
    }
)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue
    private Long id;

    private String orderId;

    private int amount;

    private String eventId;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
}
