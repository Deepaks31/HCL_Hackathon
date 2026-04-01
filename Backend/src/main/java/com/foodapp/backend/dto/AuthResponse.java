package com.foodapp.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String message;
    private String role;
    private Long id;
    private Long restaurantId;
    
    public AuthResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }
}
