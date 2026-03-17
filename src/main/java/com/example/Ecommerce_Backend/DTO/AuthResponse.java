package com.example.Ecommerce_Backend.DTO;

public class AuthResponse {
    private String name;
    private String email;
    private String role;
    private String token;
    private String message;

    public AuthResponse(){

    }

    public AuthResponse(String name, String email, String role, String token, String message) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.token = token;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
