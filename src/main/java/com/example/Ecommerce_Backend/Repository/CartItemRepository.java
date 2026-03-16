package com.example.Ecommerce_Backend.Repository;

import com.example.Ecommerce_Backend.Model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {}
