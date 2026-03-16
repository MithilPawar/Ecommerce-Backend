package com.example.Ecommerce_Backend.DTO;

public class AuthResponse {
    private String email;
    private String token;
    private String message;

    public AuthResponse(){

    }

    public AuthResponse(String email, String token, String message) {
        this.email = email;
        this.token = token;
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
