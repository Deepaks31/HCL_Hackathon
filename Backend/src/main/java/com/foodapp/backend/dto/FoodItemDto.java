package com.foodapp.backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class FoodItemDto {
    @NotBlank
    private String name;
    @NotNull
    private Double price;
    @NotNull
    private Integer availableQuantity;
    private String imageUrl;
    @NotNull
    private Long restaurantId;
}
