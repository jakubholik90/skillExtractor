// ProjectService.java
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
        log.info("Creating project: {} for user: {}", request.getProjectName(), username);

        // Validate
        validateProjectUpload(request);

        User user = userService.getUserByUsername(username);

        // Extract file info
        List<String> fileNames = request.getFiles().stream()
                .map(ProjectUploadRequest.FileData::getFilename)
                .collect(Collectors.toList());

        long totalSize = calculateTotalSize(request.getFiles());

        Project project = Project.builder()
                .name(request.getProjectName())
                .description(request.getDescription())
                .analyzedFiles(String.join(",", fileNames))
                .totalFiles(fileNames.size())
                .totalSizeKb(totalSize / 1024)
                .user(user)
                .build();

        return projectRepository.save(project);
    }

    public List<Project> getUserProjects(String username) {
        User user = userService.getUserByUsername(username);
        return projectRepository.findByUserIdOrderByUploadedAtDesc(user.getId());
    }

    public Project getProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));
    }

    @Transactional
    public void deleteProject(Long projectId, String username) {
        Project project = getProjectById(projectId);
        User user = userService.getUserByUsername(username);

        if (!project.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to delete this project");
        }

        projectRepository.delete(project);
    }

    private void validateProjectUpload(ProjectUploadRequest request) {
        if (request.getFiles() == null || request.getFiles().isEmpty()) {
            throw new RuntimeException("No files provided");
        }

        if (request.getFiles().size() > maxFiles) {
            throw new RuntimeException("Maximum " + maxFiles + " files allowed");
        }

        long totalSize = calculateTotalSize(request.getFiles());
        long maxSizeBytes = maxSizeMb * 1024 * 1024;

        if (totalSize > maxSizeBytes) {
            throw new RuntimeException("Total file size exceeds " + maxSizeMb + "MB");
        }
    }

    private long calculateTotalSize(List<ProjectUploadRequest.FileData> files) {
        return files.stream()
                .mapToLong(f -> f.getContent().length())
                .sum();
    }
}

// ============================================