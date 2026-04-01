package com.foodapp.backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class CartDto {
    @NotNull
    private Long userId;
    @NotNull
    private Long foodItemId;
    @NotNull
    private Integer quantity;
}
