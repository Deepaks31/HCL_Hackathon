package com.foodapp.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    private String password;
    
    @Enumerated(EnumType.STRING)
    private Role role;
    
    @Enumerated(EnumType.STRING)
    private Status status;
    
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL)
    private Restaurant restaurant;
}
