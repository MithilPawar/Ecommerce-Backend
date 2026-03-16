package com.example.Ecommerce_Backend.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record OrderRequestDTO(
        @NotBlank(message = "Address line 1 is required")
        String addressLine1,
        String addressLine2,

        @NotBlank(message = "City is required!")
        String city,

        @NotBlank(message = "State is required")
        String state,

        @NotBlank(message = "Pin code is required")
        String pinCode,

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number is not valid")
        String phoneNumber
) { }
