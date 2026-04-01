package com.foodapp.backend.service;

import com.foodapp.backend.dto.AuthResponse;
import com.foodapp.backend.dto.LoginRequest;
import com.foodapp.backend.dto.RegisterRequest;
import com.foodapp.backend.entity.*;
import com.foodapp.backend.repository.EmailVerificationRepository;
import com.foodapp.backend.repository.RestaurantRepository;
import com.foodapp.backend.repository.UserRepository;
import com.foodapp.backend.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public AuthService(UserRepository userRepository, RestaurantRepository restaurantRepository,
                       EmailVerificationRepository emailVerificationRepository, PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil, AuthenticationManager authenticationManager, EmailService emailService) {
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.emailVerificationRepository = emailVerificationRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setStatus(Status.APPROVED);
        userRepository.save(user);

        return new AuthResponse(null, "Registration successful");
    }

    @Transactional
    public AuthResponse registerOwner(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.OWNER);
        user.setStatus(Status.PENDING);
        userRepository.save(user);

        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getRestaurantName());
        restaurant.setLocation(request.getLocation());
        restaurant.setOwner(user);
        restaurant.setStatus(Status.PENDING);
        restaurantRepository.save(restaurant);

        String token = UUID.randomUUID().toString();
        EmailVerification verification = new EmailVerification();
        verification.setUserId(user.getId());
        verification.setToken(token);
        verification.setVerified(false);
        emailVerificationRepository.save(verification);

        emailService.sendVerificationEmail(user.getEmail(), token);

        return new AuthResponse(null, "Owner registered successfully. Please check your console/email for verification link.");
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() == Role.OWNER) {
            EmailVerification verify = emailVerificationRepository.findByUserId(user.getId())
                    .orElse(null);
            if (verify != null && !verify.isVerified()) {
                throw new RuntimeException("Email not verified");
            }
            if (user.getStatus() == Status.PENDING) {
                throw new RuntimeException("Admin approval pending");
            }
            if (user.getStatus() == Status.REJECTED) {
                throw new RuntimeException("Account rejected by admin");
            }
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String jwt = jwtUtil.generateToken(new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), java.util.Collections.emptyList()
        ));

        Long resId = null;
        if (user.getRole() == Role.OWNER && user.getRestaurant() != null) {
            resId = user.getRestaurant().getId();
        }
        return new AuthResponse(jwt, "Login successful", user.getRole().name(), user.getId(), resId);
    }

    @Transactional
    public String verifyEmail(String token) {
        EmailVerification verification = emailVerificationRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (verification.isVerified()) {
            return "Email is already verified";
        }

        verification.setVerified(true);
        emailVerificationRepository.save(verification);
        return "Email verified successfully. Awaiting admin approval.";
    }
}
