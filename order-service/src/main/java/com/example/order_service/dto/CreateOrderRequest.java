package com.example.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateOrderRequest {
    private int amount;
}
