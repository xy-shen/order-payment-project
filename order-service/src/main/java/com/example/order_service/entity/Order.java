package com.example.order_service.entity;

import com.example.order_service.util.OrderStatus;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {
    @Id
    String id;

    int amount;

    @Indexed
    OrderStatus status;

    @CreatedDate
    LocalDateTime createdAt;

    @LastModifiedDate
    LocalDateTime updatedAt;
}
