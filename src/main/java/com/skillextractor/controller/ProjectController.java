// ProjectController.java - With improved DELETE handling
package com.skillextractor.controller;

import com.skillextractor.dto.ProjectUploadRequest;
import com.skillextractor.dto.SkillResponse;
import com.skillextractor.model.Project;
import com.skillextractor.service.ProjectService;
import com.skillextractor.service.SkillAnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Slf4j
public class ProjectController {

    private final ProjectService projectService;
    private final SkillAnalysisService skillAnalysisService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadProject(
            @RequestBody ProjectUploadRequest request,
            Authentication authentication) {

        String username = authentication.getName();
        log.info("Upload request from user: {}", username);

        try {
            // Create project
            Project project = projectService.createProject(request, username);
            log.info("Project created with ID: {}", project.getId());

            // Analyze and extract skills
            List<SkillResponse> skills = skillAnalysisService.analyzeAndSaveSkills(
                    request, project, username);
            log.info("Extracted {} skills from project", skills.size());

            // Build response
            Map<String, Object> response = new HashMap<>();
            response.put("project", Map.of(
                    "id", project.getId(),
                    "name", project.getName(),
                    "description", project.getDescription() != null ? project.getDescription() : "",
                    "totalFiles", project.getTotalFiles(),
                    "uploadedAt", project.getUploadedAt().toString()
            ));
            response.put("skills", skills);
            response.put("message", "Project uploaded and analyzed successfully");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error uploading project", e);
            throw new RuntimeException("Failed to upload project: " + e.getMessage());
        }
    }

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<Project>> getUserProjects(Authentication authentication) {
        String username = authentication.getName();
        log.info("Fetching projects for user: {}", username);

        List<Project> projects = projectService.getUserProjects(username);
        log.info("Returning {} projects", projects.size());

        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<Project> getProject(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        log.info("GET project ID: {} by user: {}", id, username);

        Project project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProject(
            @PathVariable Long id,
            Authentication authentication) {

        String username = authentication.getName();
        log.info("DELETE request - Project ID: {}, User: {}", id, username);

        try {
            projectService.deleteProject(id, username);
            log.info("Project {} deleted successfully by user: {}", id, username);

            // Return JSON response instead of plain string
            Map<String, String> response = new HashMap<>();
            response.put("message", "Project deleted successfully");
            response.put("projectId", id.toString());

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            log.error("Failed to delete project {}: {}", id, e.getMessage());
            throw e;
        }
    }
}