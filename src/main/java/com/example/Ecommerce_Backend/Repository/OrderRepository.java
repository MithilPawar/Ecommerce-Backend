package com.example.Ecommerce_Backend.Repository;

import com.example.Ecommerce_Backend.Model.Order;
import com.example.Ecommerce_Backend.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(Users user);
}
