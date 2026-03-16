package com.example.Ecommerce_Backend.DTO;

public record OrderItemDTO(
        Long productId,
        String productName,
        int quantity,
        double price
) { }
