package com.foodapp.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class EmailVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id")
    private Long userId;
    
    private String token;
    
    private boolean isVerified;
}
