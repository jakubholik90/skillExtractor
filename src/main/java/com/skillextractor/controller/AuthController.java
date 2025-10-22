// AuthController.java
package com.skillextractor.controller;

import com.skillextractor.dto.AuthResponse;
import com.skillextractor.dto.UserRegistrationRequest;
import com.skillextractor.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody UserRegistrationRequest request) {
        AuthResponse response = userService.registerUser(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/login")
    public ResponseEntity<String> login() {
        // Basic auth is handled by Spring Security
        return ResponseEntity.ok("Login successful");
    }
}

// ============================================

