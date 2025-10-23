// SkillAnalysisService.java
package com.skillextractor.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillextractor.dto.ProjectUploadRequest;
import com.skillextractor.dto.SkillResponse;
import com.skillextractor.enums.SkillCategory;
import com.skillextractor.model.Project;
import com.skillextractor.model.Skill;
import com.skillextractor.model.User;
import com.skillextractor.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SkillAnalysisService {

    private final SkillRepository skillRepository;
    private final OpenAIService openAIService;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Transactional
    public List<SkillResponse> analyzeAndSaveSkills(
            ProjectUploadRequest request,
            Project project,
            String username) {

        log.info("Analyzing project {} for skills", project.getName());

        User user = userService.getUserByUsername(username);

        // Combine all file contents
        String projectCode = combineFileContents(request.getFiles());
        List<String> fileNames = request.getFiles().stream()
                .map(ProjectUploadRequest.FileData::getFilename)
                .collect(Collectors.toList());

        // Call OpenAI for analysis
        String analysisResponse = openAIService.analyzeProjectForSkills(projectCode, fileNames);

        // Parse JSON response
        List<Skill> skills = parseSkillsFromResponse(analysisResponse, project, user);

        // Save all skills
        skills = skillRepository.saveAll(skills);

        // Convert to response DTOs
        return skills.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<SkillResponse> getUserSkills(String username) {
        User user = userService.getUserByUsername(username);
        return skillRepository.findByUserId(user.getId()).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<SkillResponse> getUserSkillsByCategory(String username, SkillCategory category) {
        User user = userService.getUserByUsername(username);
        return skillRepository.findByUserIdAndCategory(user.getId(), category).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public SkillResponse getSkillById(Long skillId) {
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new RuntimeException("Skill not found: " + skillId));
        return convertToResponse(skill);
    }

    private String combineFileContents(List<ProjectUploadRequest.FileData> files) {
        StringBuilder combined = new StringBuilder();
        for (ProjectUploadRequest.FileData file : files) {
            combined.append("// ========== ").append(file.getFilename()).append(" ==========\n");
            combined.append(file.getContent()).append("\n\n");
        }
        return combined.toString();
    }

    private List<Skill> parseSkillsFromResponse(String jsonResponse, Project project, User user) {
        List<Skill> skills = new ArrayList<>();

        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode skillsNode = root.get("skills");

            if (skillsNode != null && skillsNode.isArray()) {
                for (JsonNode skillNode : skillsNode) {
                    Skill skill = Skill.builder()
                            .name(skillNode.get("name").asText())
                            .category(SkillCategory.valueOf(skillNode.get("category").asText()))
                            .description(skillNode.get("description").asText())
                            .exampleUsage(skillNode.get("exampleUsage").asText())
                            .isGeneral(skillNode.has("isGeneral") && skillNode.get("isGeneral").asBoolean())
                            .project(project)
                            .user(user)
                            .build();

                    skills.add(skill);
                }
            }
        } catch (Exception e) {
            log.error("Error parsing skills from OpenAI response", e);
            throw new RuntimeException("Failed to parse skills from AI response");
        }

        return skills;
    }

    private SkillResponse convertToResponse(Skill skill) {
        return SkillResponse.builder()
                .id(skill.getId())
                .name(skill.getName())
                .category(skill.getCategory())
                .categoryDisplayName(skill.getCategory().getDisplayName())
                .description(skill.getDescription())
                .exampleUsage(skill.getExampleUsage())
                .level(skill.getLevel())
                .levelDisplay(skill.getLevel().getDisplayText())
                .isGeneral(skill.getIsGeneral())
                .projectName(skill.getProject().getName())
                .createdAt(skill.getCreatedAt())
                .build();
    }
}