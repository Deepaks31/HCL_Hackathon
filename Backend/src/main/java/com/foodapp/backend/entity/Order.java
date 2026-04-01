package com.foodapp.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Restaurant restaurant;

    @Transient
    @com.fasterxml.jackson.annotation.JsonProperty("restaurantName")
    public String fetchRestaurantName() {
        return restaurant != null ? restaurant.getName() : null;
    }
    
    private Double totalAmount;
    
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    public List<OrderItem> getItems() {
        return items;
    }
}
