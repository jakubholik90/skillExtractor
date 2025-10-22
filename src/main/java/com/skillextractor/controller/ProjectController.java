// ProjectController.java
package com.skillextractor.controller;

import com.skillextractor.dto.ProjectUploadRequest;
import com.skillextractor.dto.SkillResponse;
import com.skillextractor.model.Project;
import com.skillextractor.service.ProjectService;
import com.skillextractor.service.SkillAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final SkillAnalysisService skillAnalysisService;

    @PostMapping("/upload")
    public ResponseEntity<List<SkillResponse>> uploadProject(
            @RequestBody ProjectUploadRequest request,
            Authentication authentication) {

        String username = authentication.getName();

        // Create project
        Project project = projectService.createProject(request, username);

        // Analyze and extract skills
        List<SkillResponse> skills = skillAnalysisService.analyzeAndSaveSkills(
                request, project, username);

        return ResponseEntity.ok(skills);
    }

    @GetMapping
    public ResponseEntity<List<Project>> getUserProjects(Authentication authentication) {
        String username = authentication.getName();
        List<Project> projects = projectService.getUserProjects(username);
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

