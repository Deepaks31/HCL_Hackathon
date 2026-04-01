package com.foodapp.backend;

import com.foodapp.backend.entity.Role;
import com.foodapp.backend.entity.Status;
import com.foodapp.backend.entity.User;
import com.foodapp.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Bean
	CommandLineRunner run(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {
				User admin = new User();
				admin.setName("Default Admin");
				admin.setEmail("admin@gmail.com");
				admin.setPassword(passwordEncoder.encode("admin"));
				admin.setRole(Role.ADMIN);
				admin.setStatus(Status.APPROVED);
				userRepository.save(admin);
				System.out.println("==================================================");
				System.out.println("DEFAULT ADMIN CREATED: admin@gmail.com / admin");
				System.out.println("==================================================");
			}
		};
	}
}
