package com.example.Ecommerce_Backend.Service;

import com.example.Ecommerce_Backend.DTO.CartItemDTO;
import com.example.Ecommerce_Backend.DTO.CartResponseDTO;
import com.example.Ecommerce_Backend.Exception.CustomExceptionHandler.*;
import com.example.Ecommerce_Backend.Model.Cart;
import com.example.Ecommerce_Backend.Model.CartItem;
import com.example.Ecommerce_Backend.Model.Products;
import com.example.Ecommerce_Backend.Model.Users;
import com.example.Ecommerce_Backend.Repository.CartItemRepository;
import com.example.Ecommerce_Backend.Repository.CartRepository;
import com.example.Ecommerce_Backend.Repository.ProductRepository;
import com.example.Ecommerce_Backend.Repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartService {
    CartRepository cartRepository;
    CartItemRepository cartItemRepository;
    UserRepository userRepository;
    ProductRepository productRepository;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    private CartResponseDTO mapToDTO(Cart cart){
        List<CartItemDTO> itemDTOS = cart.getCartItems().stream()
                .map(cartItem -> new CartItemDTO(
                        cartItem.getProducts().getId(),
                        cartItem.getProducts().getName(),
                        cartItem.getQuantity(),
                        cartItem.getPrice()
                ))
                .toList();
        return new CartResponseDTO(cart.getId(), itemDTOS, cart.getTotalPrice());
    }

    public Cart getCart(Users user){
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }

    public CartResponseDTO getCartDTO(Users user){
        Cart cart = getCart(user); // reuse existing method
        return mapToDTO(cart);
    }


    public CartResponseDTO addToCart(Users user, Long productId, int quantity){
        Cart cart = getCart(user);
        Products products = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProducts().getId().equals(productId))
                .findFirst();

        if(existingCartItem.isPresent()){
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setPrice(cartItem.getQuantity() * products.getPrice());
        }else{
            CartItem cartItem = new CartItem();
            cartItem.setProducts(products);
            cartItem.setQuantity(quantity);
            cartItem.setPrice(quantity * products.getPrice());
            cartItem.setCart(cart);
            cart.getCartItems().add(cartItem);
        }
        cart.calculatePrice();
        return mapToDTO(cartRepository.save(cart));
    }

    public CartResponseDTO removeCart(Users user, Long productId){
        Cart cart = getCart(user);
        cart.getCartItems().removeIf(cartItem -> cartItem.getProducts().getId().equals(productId));
        cart.calculatePrice();
        return mapToDTO(cartRepository.save(cart));
    }

    public void clearCart(Users user){
        Cart cart = getCart(user);
        cart.getCartItems().clear();
        cart.setTotalPrice(0);
        cartRepository.save(cart);
    }
}
