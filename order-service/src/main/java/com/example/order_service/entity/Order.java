package com.example.order_service.entity;

import com.example.order_service.util.OrderStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    String id;

    int amount;

    OrderStatus status;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
