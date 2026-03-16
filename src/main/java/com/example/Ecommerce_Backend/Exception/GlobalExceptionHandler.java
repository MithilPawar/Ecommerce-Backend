package com.example.Ecommerce_Backend.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    //handling validation error
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex){
        Map<String, Object> response = new HashMap<>();
        response.put("timestamps", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validation error");

        //Collecting errors specific to the field
        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                validationErrors.put(error.getField(), error.getDefaultMessage())
        );

        response.put("message", validationErrors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //handling generic runtime exceptions
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeExceptions(RuntimeException ex){
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Bad request");
        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String error, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", error);
        response.put("message", message);
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(CustomExceptionHandler.CartNotFoundExceptions.class)
    public ResponseEntity<Map<String, Object>> handleCartNotFound(CustomExceptionHandler.CartNotFoundExceptions ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Cart Not Found", ex.getMessage());
    }

    @ExceptionHandler(CustomExceptionHandler.OrderNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleOrderNotFound(CustomExceptionHandler.OrderNotFoundException ex){
        return buildResponse(HttpStatus.NOT_FOUND, "Order not found", ex.getMessage());
    }

    @ExceptionHandler(CustomExceptionHandler.ProductNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleProductNotFound(CustomExceptionHandler.ProductNotFoundException ex){
        return buildResponse(HttpStatus.NOT_FOUND, "Product not found", ex.getMessage());
    }

    @ExceptionHandler(CustomExceptionHandler.UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFound(CustomExceptionHandler.UserNotFoundException ex){
        return buildResponse(HttpStatus.NOT_FOUND, "User not found", ex.getMessage());
    }

    @ExceptionHandler(CustomExceptionHandler.CategoryNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCategoryNotFound(CustomExceptionHandler.CategoryNotFoundException ex){
        return buildResponse(HttpStatus.NOT_FOUND, "Category not found", ex.getMessage());
    }
}
