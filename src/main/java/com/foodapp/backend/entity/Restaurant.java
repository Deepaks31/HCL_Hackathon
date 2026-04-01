package com.foodapp.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String location;
    
    @OneToOne
    @JoinColumn(name = "owner_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private User owner;
    
    @Enumerated(EnumType.STRING)
    private Status status;
}
