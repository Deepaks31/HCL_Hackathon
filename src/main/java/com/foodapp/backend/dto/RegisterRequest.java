package com.foodapp.backend.dto;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import com.foodapp.backend.entity.Role;

@Data
public class RegisterRequest {
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
    
    private Role role;
    
    // For Owner
    private String restaurantName;
    private String location;
}
