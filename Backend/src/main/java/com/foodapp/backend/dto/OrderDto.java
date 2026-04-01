package com.foodapp.backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class OrderDto {
    @NotNull
    private Long userId;
    @NotNull
    private Long restaurantId;
}
