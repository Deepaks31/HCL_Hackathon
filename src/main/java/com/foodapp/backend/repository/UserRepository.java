package com.foodapp.backend.repository;

import com.foodapp.backend.entity.User;
import com.foodapp.backend.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByStatus(Status status);
}
