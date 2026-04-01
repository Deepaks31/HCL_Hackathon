package com.foodapp.backend.controller;

import com.foodapp.backend.dto.AuthResponse;
import com.foodapp.backend.dto.FoodItemDto;
import com.foodapp.backend.dto.RegisterRequest;
import com.foodapp.backend.entity.FoodItem;
import com.foodapp.backend.entity.Order;
import com.foodapp.backend.service.AuthService;
import com.foodapp.backend.service.OwnerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/owner")
@CrossOrigin(origins = "*")
public class OwnerController {

    private final AuthService authService;
    private final OwnerService ownerService;

    public OwnerController(AuthService authService, OwnerService ownerService) {
        this.authService = authService;
        this.ownerService = ownerService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> registerOwner(@Valid @RequestBody RegisterRequest request) {
        try {
            return ResponseEntity.ok(authService.registerOwner(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new AuthResponse(null, e.getMessage()));
        }
    }

    @PostMapping("/foods")
    public ResponseEntity<FoodItem> addFoodItem(@Valid @RequestBody FoodItemDto dto) {
        return ResponseEntity.ok(ownerService.addFoodItem(dto));
    }

    @PutMapping("/foods/{id}")
    public ResponseEntity<FoodItem> updateFoodItem(@PathVariable Long id, @Valid @RequestBody FoodItemDto dto) {
        return ResponseEntity.ok(ownerService.updateFoodItem(id, dto));
    }

    @DeleteMapping("/foods/{id}")
    public ResponseEntity<Void> deleteFoodItem(@PathVariable Long id) {
        try {
            ownerService.deleteFoodItem(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/orders/{restaurantId}")
    public ResponseEntity<List<Order>> getOrders(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(ownerService.getOrders(restaurantId));
    }
}
