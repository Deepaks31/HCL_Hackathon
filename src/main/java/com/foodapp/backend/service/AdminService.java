package com.foodapp.backend.service;

import com.foodapp.backend.entity.Restaurant;
import com.foodapp.backend.entity.Status;
import com.foodapp.backend.entity.User;
import com.foodapp.backend.repository.RestaurantRepository;
import com.foodapp.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    public AdminService(UserRepository userRepository, RestaurantRepository restaurantRepository) {
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public List<User> getPendingOwners() {
        return userRepository.findByStatus(Status.PENDING);
    }

    @Transactional
    public String approveOwner(Long ownerId) {
        User user = userRepository.findById(ownerId).orElseThrow(() -> new RuntimeException("Owner not found"));
        user.setStatus(Status.APPROVED);
        userRepository.save(user);

        Restaurant restaurant = user.getRestaurant();
        if (restaurant != null) {
            restaurant.setStatus(Status.APPROVED);
            restaurantRepository.save(restaurant);
        }

        // Mock notify email
        System.out.println("Mock Email to " + user.getEmail() + ": Your account has been APPROVED.");

        return "Owner approved successfully";
    }

    @Transactional
    public String rejectOwner(Long ownerId) {
        User user = userRepository.findById(ownerId).orElseThrow(() -> new RuntimeException("Owner not found"));
        user.setStatus(Status.REJECTED);
        userRepository.save(user);

        Restaurant restaurant = user.getRestaurant();
        if (restaurant != null) {
            restaurant.setStatus(Status.REJECTED);
            restaurantRepository.save(restaurant);
        }

        System.out.println("Mock Email to " + user.getEmail() + ": Your account has been REJECTED.");

        return "Owner rejected successfully";
    }
}
