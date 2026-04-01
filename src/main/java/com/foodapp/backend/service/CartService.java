package com.foodapp.backend.service;

import com.foodapp.backend.dto.CartDto;
import com.foodapp.backend.entity.Cart;
import com.foodapp.backend.entity.CartItem;
import com.foodapp.backend.entity.FoodItem;
import com.foodapp.backend.entity.User;
import com.foodapp.backend.repository.CartItemRepository;
import com.foodapp.backend.repository.CartRepository;
import com.foodapp.backend.repository.FoodItemRepository;
import com.foodapp.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final FoodItemRepository foodItemRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,
                       FoodItemRepository foodItemRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.foodItemRepository = foodItemRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Cart addToCart(CartDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        FoodItem foodItem = foodItemRepository.findById(dto.getFoodItemId())
                .orElseThrow(() -> new RuntimeException("Food item not found"));

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart c = new Cart();
                    c.setUser(user);
                    return cartRepository.save(c);
                });

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setFoodItem(foodItem);
        item.setQuantity(dto.getQuantity());

        cart.getItems().add(item);
        return cartRepository.save(cart);
    }

    @Transactional
    public void removeFromCart(Long itemId) {
        cartItemRepository.deleteById(itemId);
    }

    public Cart getCart(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }
}
