package com.example.Ecommerce_Backend.DTO;

import java.util.List;

public record CartResponseDTO(
        Long cartId,
        List<CartItemDTO> items,
        double totalPrice) {
}
