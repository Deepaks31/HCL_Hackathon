package com.foodapp.backend.controller;

import com.foodapp.backend.entity.FoodItem;
import com.foodapp.backend.entity.Restaurant;
import com.foodapp.backend.service.RestaurantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
@CrossOrigin(origins = "*")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public ResponseEntity<List<Restaurant>> getApprovedRestaurants() {
        return ResponseEntity.ok(restaurantService.getApprovedRestaurants());
    }

    @GetMapping("/{id}/menu")
    public ResponseEntity<List<FoodItem>> getMenu(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getMenu(id));
    }
}
