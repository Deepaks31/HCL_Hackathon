package com.foodapp.backend.service;

import com.foodapp.backend.entity.FoodItem;
import com.foodapp.backend.entity.Restaurant;
import com.foodapp.backend.entity.Status;
import com.foodapp.backend.repository.FoodItemRepository;
import com.foodapp.backend.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final FoodItemRepository foodItemRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, FoodItemRepository foodItemRepository) {
        this.restaurantRepository = restaurantRepository;
        this.foodItemRepository = foodItemRepository;
    }

    public List<Restaurant> getApprovedRestaurants() {
        return restaurantRepository.findByStatus(Status.APPROVED);
    }

    public List<FoodItem> getMenu(Long restaurantId) {
        return foodItemRepository.findByRestaurantId(restaurantId);
    }
}
