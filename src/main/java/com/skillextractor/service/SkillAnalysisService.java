// SkillAnalysisService.java - UPDATED VERSION (key parts)
// Update the getSkillById method and parseSkillsFromResponse error handling

import com.skillextractor.exception.*;
import com.skillextractor.model.User;

import java.util.List;

public SkillResponse getSkillById(Long skillId) {
    Skill skill = skillRepository.findById(skillId)
            .orElseThrow(() -> new SkillNotFoundException(skillId));
    return convertToResponse(skill);
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
        throw new SkillAnalysisException("Failed to parse AI response", e);
    }

    return skills;
}

// ============================================

