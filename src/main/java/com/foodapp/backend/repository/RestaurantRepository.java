package com.foodapp.backend.repository;

import com.foodapp.backend.entity.Restaurant;
import com.foodapp.backend.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findByStatus(Status status);
}
