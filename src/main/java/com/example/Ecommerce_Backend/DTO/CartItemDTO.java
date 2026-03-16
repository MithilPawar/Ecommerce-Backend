package com.example.Ecommerce_Backend.DTO;

public record CartItemDTO(
        Long productId,
        String productName,
        int quantity,
        double price) {
}
