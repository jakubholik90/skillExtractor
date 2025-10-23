// ProjectService.java - DEBUG VERSION
package com.skillextractor.service;

import com.skillextractor.dto.ProjectUploadRequest;
import com.skillextractor.model.Project;
import com.skillextractor.model.User;
import com.skillextractor.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserService userService;

    @Value("${project.max.files:20}")
    private int maxFiles;

    @Value("${project.max.size.mb:10}")
    private long maxSizeMb;

    @Transactional
    public Project createProject(ProjectUploadRequest request, String username) {
        log.info(">>> createProject() START");
        log.info("Project Name: {}", request.getProjectName());
        log.info("Description: {}", request.getDescription());
        log.info("Username: {}", username);

        // Validate
        validateProjectUpload(request);
        log.info("Validation passed");

        // Get user
        User user = userService.getUserByUsername(username);
        log.info("User found - ID: {}, Username: {}", user.getId(), user.getUsername());

        // Extract file info
        List<String> fileNames = request.getFiles().stream()
                .map(ProjectUploadRequest.FileData::getFilename)
                .collect(Collectors.toList());
        log.info("File names: {}", fileNames);

        long totalSize = calculateTotalSize(request.getFiles());
        log.info("Total size: {} bytes ({} KB)", totalSize, totalSize / 1024);

        // Build project
        Project project = Project.builder()
                .name(request.getProjectName())
                .description(request.getDescription())
                .analyzedFiles(String.join(",", fileNames))
                .totalFiles(fileNames.size())
                .totalSizeKb(totalSize / 1024)
                .user(user)
                .build();

        log.info("Project object created (before save)");
        log.info("  - Name: {}", project.getName());
        log.info("  - Description: {}", project.getDescription());
        log.info("  - User ID: {}", project.getUser().getId());
        log.info("  - Total Files: {}", project.getTotalFiles());

        // Save project
        log.info("Calling projectRepository.save()...");
        project = projectRepository.save(project);

        log.info(">>> PROJECT SAVED SUCCESSFULLY <<<");
        log.info("  - Project ID: {}", project.getId());
        log.info("  - Project Name: {}", project.getName());
        log.info("  - User ID: {}", project.getUser().getId());
        log.info("  - Uploaded At: {}", project.getUploadedAt());
        log.info(">>> createProject() END");

        return project;
    }

    @Transactional(readOnly = true)
    public List<Project> getUserProjects(String username) {
        log.info(">>> getUserProjects() START");
        log.info("Username: {}", username);

        User user = userService.getUserByUsername(username);
        log.info("User found - ID: {}", user.getId());

        log.info("Calling projectRepository.findByUserIdOrderByUploadedAtDesc()...");
        List<Project> projects = projectRepository.findByUserIdOrderByUploadedAtDesc(user.getId());

        log.info(">>> PROJECTS FOUND: {} <<<", projects.size());

        if (projects.isEmpty()) {
            log.warn("!!! NO PROJECTS FOUND FOR USER ID: {} !!!", user.getId());

            // Additional check - count all projects in DB
            long totalProjects = projectRepository.count();
            log.info("Total projects in database: {}", totalProjects);

            // Check if there are any projects for this user (different query)
            List<Project> allUserProjects = projectRepository.findByUserId(user.getId());
            log.info("Projects with findByUserId: {}", allUserProjects.size());
        } else {
            log.info("Projects list:");
            projects.forEach(p -> {
                log.info("  - ID: {}, Name: {}, User ID: {}, Date: {}",
                        p.getId(), p.getName(), p.getUser().getId(), p.getUploadedAt());
            });
        }

        log.info(">>> getUserProjects() END");
        return projects;
    }

    @Transactional(readOnly = true)
    public Project getProjectById(Long projectId) {
        log.info(">>> getProjectById() - ID: {}", projectId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));
        log.info("Project found: {}", project.getName());
        return project;
    }

    @Transactional
    public void deleteProject(Long projectId, String username) {
        log.info(">>> deleteProject() START - ID: {}", projectId);

        Project project = getProjectById(projectId);
        User user = userService.getUserByUsername(username);

        log.info("Project User ID: {}, Request User ID: {}",
                project.getUser().getId(), user.getId());

        if (!project.getUser().getId().equals(user.getId())) {
            log.error("UNAUTHORIZED: User {} tried to delete project owned by user {}",
                    user.getId(), project.getUser().getId());
            throw new RuntimeException("Unauthorized to delete this project");
        }

        log.info("Deleting project: {}", projectId);
        projectRepository.delete(project);
        log.info(">>> deleteProject() END - Project deleted");
    }

    private void validateProjectUpload(ProjectUploadRequest request) {
        log.info("Validating project upload...");

        if (request.getFiles() == null || request.getFiles().isEmpty()) {
            log.error("Validation failed: No files provided");
            throw new RuntimeException("No files provided");
        }

        if (request.getFiles().size() > maxFiles) {
            log.error("Validation failed: Too many files ({} > {})", request.getFiles().size(), maxFiles);
            throw new RuntimeException("Maximum " + maxFiles + " files allowed");
        }

        long totalSize = calculateTotalSize(request.getFiles());
        long maxSizeBytes = maxSizeMb * 1024 * 1024;

        if (totalSize > maxSizeBytes) {
            log.error("Validation failed: File size too large ({} > {} bytes)", totalSize, maxSizeBytes);
            throw new RuntimeException("Total file size exceeds " + maxSizeMb + "MB");
        }

        log.info("Validation successful");
    }

    private long calculateTotalSize(List<ProjectUploadRequest.FileData> files) {
        return files.stream()
                .mapToLong(f -> f.getContent().length())
                .sum();
    }
}