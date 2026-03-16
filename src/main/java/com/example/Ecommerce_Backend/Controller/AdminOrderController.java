package com.example.Ecommerce_Backend.Controller;

import com.example.Ecommerce_Backend.DTO.OrderResponseDTO;
import com.example.Ecommerce_Backend.Enum.OrderStatus;
import com.example.Ecommerce_Backend.Service.OrderService;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@Validated
public class AdminOrderController {
    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

//    getting all the orders
    @GetMapping
    public List<OrderResponseDTO> getAllOrders(){
        return orderService.getAllOrders();
    }

//    get order by id
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderByid(@PathVariable Long id){
        OrderResponseDTO responseDTO = orderService.getOrderById(id);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

//    Updating order status
    @PostMapping("/{orderId}/status")
    public ResponseEntity<OrderResponseDTO> updateStatus(@PathVariable @Min(value = 1, message = "orderId must be >= 1") Long orderId, @RequestParam OrderStatus status){
        OrderResponseDTO responseDTO = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

}
