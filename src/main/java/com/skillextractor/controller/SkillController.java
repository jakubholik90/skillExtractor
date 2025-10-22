// SkillController.java
package com.skillextractor.controller;

import com.skillextractor.dto.SkillResponse;
import com.skillextractor.enums.SkillCategory;
import com.skillextractor.service.SkillAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillAnalysisService skillAnalysisService;

    @GetMapping
    public ResponseEntity<List<SkillResponse>> getAllSkills(Authentication authentication) {
        String username = authentication.getName();
        List<SkillResponse> skills = skillAnalysisService.getUserSkills(username);
        return ResponseEntity.ok(skills);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<SkillResponse>> getSkillsByCategory(
            @PathVariable SkillCategory category,
            Authentication authentication) {

        String username = authentication.getName();
        List<SkillResponse> skills = skillAnalysisService.getUserSkillsByCategory(username, category);
        return ResponseEntity.ok(skills);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SkillResponse> getSkill(@PathVariable Long id) {
        SkillResponse skill = skillAnalysisService.getSkillById(id);
        return ResponseEntity.ok(skill);
    }
}

// ============================================

