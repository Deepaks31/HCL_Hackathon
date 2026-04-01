package com.foodapp.backend.service;

import com.foodapp.backend.dto.OrderDto;
import com.foodapp.backend.entity.*;
import com.foodapp.backend.repository.CartRepository;
import com.foodapp.backend.repository.OrderRepository;
import com.foodapp.backend.repository.RestaurantRepository;
import com.foodapp.backend.repository.UserRepository;
import com.foodapp.backend.repository.FoodItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final FoodItemRepository foodItemRepository;

    public OrderService(OrderRepository orderRepository, CartRepository cartRepository,
                        RestaurantRepository restaurantRepository, UserRepository userRepository,
                        EmailService emailService, FoodItemRepository foodItemRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.foodItemRepository = foodItemRepository;
    }

    @Transactional
    public Order placeOrder(OrderDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Cart empty"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setRestaurant(restaurant);
        order.setStatus(OrderStatus.PLACED);

        double total = 0;
        for (CartItem ci : cart.getItems()) {
            FoodItem food = ci.getFoodItem();
            if (food.getAvailableQuantity() != null && food.getAvailableQuantity() < ci.getQuantity()) {
                throw new RuntimeException("Insufficient stock for item: " + food.getName());
            }
            if (food.getAvailableQuantity() != null) {
                food.setAvailableQuantity(food.getAvailableQuantity() - ci.getQuantity());
                foodItemRepository.save(food);
            }

            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setFoodItem(food);
            oi.setQuantity(ci.getQuantity());
            order.getItems().add(oi);
            total += food.getPrice() * ci.getQuantity();
        }
        order.setTotalAmount(total);

        Order saved = orderRepository.save(order);
        
        cart.getItems().clear();
        cartRepository.save(cart);

        try {
            emailService.sendOrderPlacedEmail(user, saved);
        } catch (Exception e) {}

        return saved;
    }

    @Transactional
    public Order cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        if (order.getStatus() != OrderStatus.CANCELLED) {
            order.setStatus(OrderStatus.CANCELLED);
            
            for (OrderItem oi : order.getItems()) {
                FoodItem food = oi.getFoodItem();
                if (food.getAvailableQuantity() != null) {
                    food.setAvailableQuantity(food.getAvailableQuantity() + oi.getQuantity());
                    foodItemRepository.save(food);
                }
            }
        }
        
        Order saved = orderRepository.save(order);
        
        try {
            emailService.sendOrderCancelledEmail(order.getUser(), saved);
        } catch (Exception e) {}
        
        return saved;
    }

    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
