package com.example.Ecommerce_Backend.Repository;

import com.example.Ecommerce_Backend.Model.Cart;
import com.example.Ecommerce_Backend.Model.CartItem;
import com.example.Ecommerce_Backend.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(Users user);
}

