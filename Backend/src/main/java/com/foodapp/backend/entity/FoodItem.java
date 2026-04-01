package com.foodapp.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class FoodItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private Double price;
    private Integer availableQuantity;
    private String imageUrl;
    
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Restaurant restaurant;

    @Transient
    @com.fasterxml.jackson.annotation.JsonProperty("restaurantId")
    public Long fetchRestaurantId() {
        return restaurant != null ? restaurant.getId() : null;
    }
}
