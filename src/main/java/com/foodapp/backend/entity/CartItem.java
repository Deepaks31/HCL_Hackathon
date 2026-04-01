package com.foodapp.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "cart_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Cart cart;
    
    @ManyToOne
    @JoinColumn(name = "food_item_id")
    private FoodItem foodItem;
    
    private Integer quantity;
}
