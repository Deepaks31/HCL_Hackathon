package com.foodapp.backend.service;

import com.foodapp.backend.dto.FoodItemDto;
import com.foodapp.backend.entity.FoodItem;
import com.foodapp.backend.entity.Order;
import com.foodapp.backend.entity.Restaurant;
import com.foodapp.backend.repository.FoodItemRepository;
import com.foodapp.backend.repository.OrderRepository;
import com.foodapp.backend.repository.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OwnerService {

    private final FoodItemRepository foodItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderRepository orderRepository;

    public OwnerService(FoodItemRepository foodItemRepository, RestaurantRepository restaurantRepository, OrderRepository orderRepository) {
        this.foodItemRepository = foodItemRepository;
        this.restaurantRepository = restaurantRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public FoodItem addFoodItem(FoodItemDto dto) {
        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        FoodItem item = new FoodItem();
        item.setName(dto.getName());
        item.setPrice(dto.getPrice());
        item.setAvailableQuantity(dto.getAvailableQuantity());
        item.setImageUrl(dto.getImageUrl());
        item.setRestaurant(restaurant);

        return foodItemRepository.save(item);
    }

    @Transactional
    public FoodItem updateFoodItem(Long id, FoodItemDto dto) {
        FoodItem item = foodItemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));
        item.setName(dto.getName());
        item.setPrice(dto.getPrice());
        item.setAvailableQuantity(dto.getAvailableQuantity());
        item.setImageUrl(dto.getImageUrl());
        return foodItemRepository.save(item);
    }

    @Transactional
    public void deleteFoodItem(Long id) {
        foodItemRepository.deleteById(id);
    }

    public List<Order> getOrders(Long restaurantId) {
        return orderRepository.findByRestaurantId(restaurantId);
    }
}
