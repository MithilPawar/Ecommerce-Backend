package com.example.Ecommerce_Backend.Exception;

public class CustomExceptionHandler {
    public static class CartNotFoundExceptions extends RuntimeException {
        public CartNotFoundExceptions(String message) {
            super(message);
        }
    }

    public static class OrderNotFoundException extends RuntimeException {
        public OrderNotFoundException(String message) {
            super(message);
        }
    }

        public static class ProductNotFoundException extends RuntimeException{
            public ProductNotFoundException(String message){
                super(message);
            }
        }

        public static class UserNotFoundException extends RuntimeException {
            public UserNotFoundException(String message) {
                super(message);
            }
        }

        public static class CategoryNotFoundException extends RuntimeException{
            public CategoryNotFoundException(String message){
                super(message);
            }
        }
    }
