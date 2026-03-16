package com.example.Ecommerce_Backend.Controller;

import com.example.Ecommerce_Backend.DTO.OrderRequestDTO;
import com.example.Ecommerce_Backend.DTO.OrderResponseDTO;
import com.example.Ecommerce_Backend.Model.Users;
import com.example.Ecommerce_Backend.Repository.UserRepository;
import com.example.Ecommerce_Backend.Service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@Validated
public class OrderController {
    private OrderService orderService;
    private UserRepository userRepository;

    public OrderController(OrderService orderService, UserRepository userRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    private Users getCurrentUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PostMapping("/place")
    public ResponseEntity<OrderResponseDTO> placeOrder(@Valid @RequestBody OrderRequestDTO requestDTO){
        OrderResponseDTO response = orderService.placeOrder(getCurrentUser(), requestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponseDTO>> getOrders(){
        List<OrderResponseDTO> responseDTOS = orderService.getOrderByUser(getCurrentUser());
        return ResponseEntity.status(HttpStatus.OK).body(responseDTOS);
    }
}
