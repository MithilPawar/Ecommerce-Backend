package com.example.Ecommerce_Backend.Service;

import com.example.Ecommerce_Backend.DTO.OrderItemDTO;
import com.example.Ecommerce_Backend.DTO.OrderRequestDTO;
import com.example.Ecommerce_Backend.DTO.OrderResponseDTO;
import com.example.Ecommerce_Backend.Enum.OrderStatus;
import com.example.Ecommerce_Backend.Exception.CustomExceptionHandler.*;
import com.example.Ecommerce_Backend.Model.Cart;
import com.example.Ecommerce_Backend.Model.Order;
import com.example.Ecommerce_Backend.Model.OrderItem;
import com.example.Ecommerce_Backend.Model.Users;
import com.example.Ecommerce_Backend.Repository.CartRepository;
import com.example.Ecommerce_Backend.Repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    public OrderService(OrderRepository orderRepository, CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
    }

    //mapper method
    private OrderResponseDTO mapToDTO(Order order){
        List<OrderItemDTO> itemDTOS = order.getItems().stream()
                .map(item -> new OrderItemDTO(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getPrice()
                )).collect(Collectors.toList());

        return new OrderResponseDTO(
                order.getId(),
                itemDTOS,
                order.getTotalPrice(),
                order.getStatus(),
                order.getOrderDate(),
                order.getAddressLine1(),
                order.getAddressLine2(),
                order.getCity(),
                order.getState(),
                order.getPinCode(),
                order.getPhoneNumber()
        );
    }

    @Transactional
    public OrderResponseDTO placeOrder(Users user, OrderRequestDTO requestDTO){
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Cart not found for user"));
        if(cart.getCartItems().isEmpty()){
            throw new CartNotFoundExceptions("Cart is empty. Cannot place order");
        }

        if(requestDTO.phoneNumber() == null || requestDTO.phoneNumber().length() < 10) {
            throw new IllegalArgumentException("Invalid phone number");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalPrice(cart.getTotalPrice());

        order.setAddressLine1(requestDTO.addressLine1());
        order.setAddressLine2(requestDTO.addressLine2());
        order.setCity(requestDTO.city());
        order.setState(requestDTO.state());
        order.setPinCode(requestDTO.pinCode());
        order.setPhoneNumber(requestDTO.phoneNumber());

//        Converting cart item to order item
        List<OrderItem> items = cart.getCartItems().stream()
                .map(item -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProduct(item.getProducts());
                    orderItem.setQuantity(item.getQuantity());
                    orderItem.setPrice(item.getPrice());
                    orderItem.setOrder(order);
                    return orderItem;
                }).collect(Collectors.toList());

        order.setItems(items);

        Order saveOrder = orderRepository.save(order);
        cart.getCartItems().clear();
        cart.setTotalPrice(0);
        cartRepository.save(cart);

        return mapToDTO(saveOrder);
    }

//    Get all orders for user
    public List<OrderResponseDTO> getOrderByUser(Users user){
        return orderRepository.findByUser(user).stream()
                .map(order -> mapToDTO(order))
                .collect(Collectors.toList());
    }

    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(order -> mapToDTO(order))
                .toList();
    }

    public OrderResponseDTO getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(order -> mapToDTO(order))
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
    }

    @Transactional
    public OrderResponseDTO updateOrderStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        order.setStatus(status);
        return mapToDTO(orderRepository.save(order));
    }
}
