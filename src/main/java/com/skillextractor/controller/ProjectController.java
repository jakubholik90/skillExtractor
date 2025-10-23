// ProjectController.java
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

            // ✅ Return both project info and skills
            Map<String, Object> response = new HashMap<>();
            response.put("project", Map.of(
                    "id", project.getId(),
                    "name", project.getName(),
                    "description", project.getDescription() != null ? project.getDescription() : "",
                    "totalFiles", project.getTotalFiles(),
                    "uploadedAt", project.getUploadedAt()
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
    public ResponseEntity<List<Project>> getUserProjects(Authentication authentication) {
        String username = authentication.getName();
        log.info("Fetching projects for user: {}", username);
        List<Project> projects = projectService.getUserProjects(username);
        log.info("Returning {} projects", projects.size());
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProject(@PathVariable Long id) {
        Project project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProject(
            @PathVariable Long id,
            Authentication authentication) {

        String username = authentication.getName();
        projectService.deleteProject(id, username);
        return ResponseEntity.ok("Project deleted successfully");
    }
}

// ============================================