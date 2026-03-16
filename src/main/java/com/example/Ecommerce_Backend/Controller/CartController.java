package com.example.Ecommerce_Backend.Controller;

import com.example.Ecommerce_Backend.DTO.CartItemDTO;
import com.example.Ecommerce_Backend.DTO.CartResponseDTO;
import com.example.Ecommerce_Backend.Model.Cart;
import com.example.Ecommerce_Backend.Model.Users;
import com.example.Ecommerce_Backend.Repository.UserRepository;
import com.example.Ecommerce_Backend.Service.CartService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    CartService cartService;
    UserRepository userRepository;

    public CartController(CartService cartService, UserRepository userRepository) {
        this.cartService = cartService;
        this.userRepository = userRepository;
    }

    private Users getCurrentUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @GetMapping
    public CartResponseDTO getCart(){
        return cartService.getCartDTO(getCurrentUser());
    }

    @PostMapping("/add")
    public CartResponseDTO addToCart(@RequestBody CartItemDTO cartItemDTO){
        Long productId = cartItemDTO.productId();
        int quantity = cartItemDTO.quantity();
        return cartService.addToCart(getCurrentUser(), productId, quantity);
    }

    @DeleteMapping("/remove/{product_id}")
    public CartResponseDTO removeCart(@PathVariable Long product_id){
        return cartService.removeCart(getCurrentUser(), product_id);
    }

    @DeleteMapping("/clear")
    public String clearCart(){
        cartService.clearCart(getCurrentUser());
        return "Cart cleared successfully";
    }
}
