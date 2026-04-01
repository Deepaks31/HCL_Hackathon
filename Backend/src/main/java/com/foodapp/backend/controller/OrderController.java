package com.foodapp.backend.controller;

import com.foodapp.backend.dto.OrderDto;
import com.foodapp.backend.entity.Order;
import com.foodapp.backend.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody OrderDto dto) {
        return ResponseEntity.ok(orderService.placeOrder(dto));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.cancelOrder(id));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getUserOrders(id));
    }
}
