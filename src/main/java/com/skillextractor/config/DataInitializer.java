package com.skillextractor.config;

import com.skillextractor.model.User;
import com.skillextractor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // Sprawdź czy użytkownik admin już istnieje
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setEmail("admin@skillextractor.com");

                userRepository.save(admin);

                System.out.println("✅ Default admin user created!");
                System.out.println("   Username: admin");
                System.out.println("   Password: admin123");
            }
        };
    }
}