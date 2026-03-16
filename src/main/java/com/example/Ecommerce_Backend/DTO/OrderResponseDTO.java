package com.example.Ecommerce_Backend.DTO;

import com.example.Ecommerce_Backend.Enum.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(
        Long orderId,
        List<OrderItemDTO> items,
        double totalPrice,
        OrderStatus status,
        LocalDateTime orderDate,
        String addressLine1,
        String addressLine2,
        String city,
        String state,
        String pinCode,
        String phoneNumber
) { }
