package com.foodapp.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "order_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Order order;
    
    @ManyToOne
    @JoinColumn(name = "food_item_id")
    private FoodItem foodItem;
    
    private Integer quantity;
}
