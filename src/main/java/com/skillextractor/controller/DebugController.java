// DebugController.java - Temporary debug endpoints
package com.skillextractor.controller;

import com.skillextractor.model.Project;
import com.skillextractor.model.User;
import com.skillextractor.repository.ProjectRepository;
import com.skillextractor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TEMPORARY DEBUG CONTROLLER
 * Use these endpoints to diagnose the project saving issue
 * DELETE THIS FILE after fixing the issue
 */
@RestController
@RequestMapping("/api/debug")
@RequiredArgsConstructor
@Slf4j
public class DebugController {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    /**
     * Get all projects in database (regardless of user)
     * Usage: GET /api/debug/all-projects
     */
    @GetMapping("/all-projects")
    public ResponseEntity<Map<String, Object>> getAllProjects() {
        log.info("DEBUG: Getting ALL projects from database");

        List<Project> allProjects = projectRepository.findAll();

        Map<String, Object> response = new HashMap<>();
        response.put("totalProjects", allProjects.size());
        response.put("projects", allProjects.stream().map(p -> Map.of(
                "id", p.getId(),
                "name", p.getName(),
                "userId", p.getUser().getId(),
                "username", p.getUser().getUsername(),
                "uploadedAt", p.getUploadedAt().toString()
        )).toList());

        log.info("Total projects in database: {}", allProjects.size());
        return ResponseEntity.ok(response);
    }

    /**
     * Get all users in database
     * Usage: GET /api/debug/all-users
     */
    @GetMapping("/all-users")
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        log.info("DEBUG: Getting ALL users from database");

        List<User> allUsers = userRepository.findAll();

        Map<String, Object> response = new HashMap<>();
        response.put("totalUsers", allUsers.size());
        response.put("users", allUsers.stream().map(u -> {
            long projectCount = projectRepository.findByUserId(u.getId()).size();
            return Map.of(
                    "id", u.getId(),
                    "username", u.getUsername(),
                    "email", u.getEmail(),
                    "projectCount", projectCount,
                    "createdAt", u.getCreatedAt().toString()
            );
        }).toList());

        log.info("Total users in database: {}", allUsers.size());
        return ResponseEntity.ok(response);
    }

    /**
     * Get current authenticated user info with projects
     * Usage: GET /api/debug/my-info
     */
    @GetMapping("/my-info")
    public ResponseEntity<Map<String, Object>> getMyInfo(Authentication authentication) {
        String username = authentication.getName();
        log.info("DEBUG: Getting info for current user: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        List<Project> myProjects = projectRepository.findByUserId(user.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("user", Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "email", user.getEmail()
        ));
        response.put("projectCount", myProjects.size());
        response.put("projects", myProjects.stream().map(p -> Map.of(
                "id", p.getId(),
                "name", p.getName(),
                "uploadedAt", p.getUploadedAt().toString()
        )).toList());

        log.info("User {} has {} projects", username, myProjects.size());
        return ResponseEntity.ok(response);
    }

    /**
     * Check database connectivity and table status
     * Usage: GET /api/debug/db-status
     */
    @GetMapping("/db-status")
    public ResponseEntity<Map<String, Object>> getDatabaseStatus() {
        log.info("DEBUG: Checking database status");

        Map<String, Object> response = new HashMap<>();

        try {
            long userCount = userRepository.count();
            long projectCount = projectRepository.count();

            response.put("status", "OK");
            response.put("userCount", userCount);
            response.put("projectCount", projectCount);
            response.put("message", "Database is accessible");

            log.info("Database status: Users={}, Projects={}", userCount, projectCount);
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("error", e.getMessage());
            log.error("Database error: {}", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
}